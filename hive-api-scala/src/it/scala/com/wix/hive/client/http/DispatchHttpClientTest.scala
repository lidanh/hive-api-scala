package com.wix.hive.client.http

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.wix.hive.model.WixAPIErrorException
import org.specs2.matcher.{MatchResult, Expectable, Matcher}
import org.specs2.mutable.{Before, SpecificationWithJUnit}
import org.specs2.time.NoTimeConversions
import dispatch.Future

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class DispatchHttpClientTest extends SpecificationWithJUnit with NoTimeConversions {
  sequential

  val wireMockServer = new WireMockServer(new WireMockConfiguration().port(8089))

  step {
    WireMock.configureFor("localhost", 8089)
    wireMockServer.start()
  }

  trait ctx extends Before {
    def before = WireMock.reset()

    val client = new DispatchHttpClient()

    val baseUrl = "http://localhost:8089"

    def beSuccess: Matcher[Future[_]] = (f: Future[_]) => Try(Await.result(f, 1.second)).isSuccess

    def haveDataForDymmy(data: String): Matcher[Dummy] = (d:Dummy) => d.data == data

    def isWixApiErrorException(errorCode: Matcher[Int], message: Matcher[Option[String]], wixErrorCode: Matcher[Option[Int]]): Matcher[WixAPIErrorException] = {
      errorCode ^^ { (_: WixAPIErrorException).errorCode aka "errorCode" } and
      message ^^ { (_: WixAPIErrorException).message aka "message" } and
      wixErrorCode ^^ { (_: WixAPIErrorException).wixErrorCode aka "wixErrorCode" }
    }

    def beFailedWith(exception: Matcher[WixAPIErrorException]) = new Matcher[Future[_]] {
      override def apply[S <: Future[_]](t: Expectable[S]): MatchResult[S] = {
        val f = t.value
        Await.ready(f, 1.second).value match {
          case Some(Failure(ex)) =>
            val res = exception(createExpectable(ex.getCause.asInstanceOf[WixAPIErrorException]))
            result(res.isSuccess, "", s"Future failed however ${res.message}", t)
          case _ => result(test = false, "", "Future didn't fail", t)
        }
      }
    }
  }


  "request" should {

    "parse response to an Object" in new ctx {
      givenThat(get(urlMatching("/test"))
        .willReturn(aResponse().withBody( """{"data":"some info"}""")))

      client.request[Dummy](HttpRequestData(HttpMethod.GET, s"$baseUrl/test")) must be_===(Dummy("some info")).await(timeout = 1.second)
    }

    "pass query parameters" in new ctx {
      givenThat(get(urlMatching("/test?.*"))
        .willReturn(aResponse()))

      client.request(HttpRequestData(HttpMethod.GET, s"$baseUrl/test", queryString = Map("a" -> "b"))) must beSuccess

      verify(getRequestedFor(urlEqualTo("/test?a=b")))
    }

    "pass headers" in new ctx {
      givenThat(get(urlMatching("/test"))
        .willReturn(aResponse()))

      client.request(HttpRequestData(HttpMethod.GET, s"$baseUrl/test", headers = Map("c" -> "d"))) must beSuccess

      verify(getRequestedFor(urlEqualTo("/test")).withHeader("c", WireMock.equalTo("d")))
    }

    "pass body" in new ctx {
      givenThat(post(urlMatching("/test"))
        .willReturn(aResponse()))

      client.request(HttpRequestData(HttpMethod.POST, s"$baseUrl/test", body = Some(Dummy("body text")))) must beSuccess

      verify(postRequestedFor(urlEqualTo("/test")).withRequestBody(equalToJson( """{"data":"body text"}""")))
    }

    "work with all method types" in new ctx {
      givenThat(get(urlEqualTo("/test")).willReturn(aResponse().withBody("""{"data":"GET"}""")))
      givenThat(post(urlEqualTo("/test")).willReturn(aResponse().withBody("""{"data":"POST"}""")))
      givenThat(put(urlEqualTo("/test")).willReturn(aResponse().withBody("""{"data":"PUT"}""")))
      givenThat(delete(urlEqualTo("/test")).willReturn(aResponse().withBody("""{"data":"DELETE"}""")))

      HttpMethod.values.foreach(method => {
        client.request[Dummy](HttpRequestData(method, s"$baseUrl/test")) must haveDataForDymmy(method.toString).await //beSuccess.updateMessage(msg => s"Failed for method $method - $msg")
      })
    }

    "handle error returned from the server" in new ctx {
      givenThat(any(urlEqualTo("/test")).willReturn(aResponse().
                                                    withStatus(400).
                                                    withBody("""{"errorCode":400,"message":"some msg","wixErrorCode":-23001}""")))

      val res = client.request(HttpRequestData(HttpMethod.GET, s"$baseUrl/test")) must beFailedWith(isWixApiErrorException(be_===(400), beSome("some msg"), beSome(-23001)))
    }

    "handle failure - no server listening" in new ctx {
      val res = client.request(HttpRequestData(HttpMethod.GET, s"$baseUrl/test")) must beFailedWith(isWixApiErrorException(be_===(404), beSome("Not Found"), beNone))
    }
  }

  step {
    wireMockServer.shutdown()
  }
}


case class Dummy(data: String)