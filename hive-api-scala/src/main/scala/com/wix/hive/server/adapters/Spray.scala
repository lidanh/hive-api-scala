package com.wix.hive.server.adapters

import com.wix.hive.client.http.HttpRequestData
import spray.http.HttpRequest

/**
 * User: maximn
 * Date: 1/13/15
 */
object Spray {

  implicit object RequestConverterFromSpray extends RequestConverterFrom[HttpRequest] {

    override def convert(req: HttpRequest): HttpRequestData = {
      val uri = req.uri.toString()
      if (uri.contains("?"))
        throw new UnsupportedOperationException(s"Bad URL: $uri. It shouldn't contain a '?' sign (Query string parameters)")

      val method = req.method match {
        case spray.http.HttpMethods.POST => com.wix.hive.client.http.HttpMethod.POST
        case unsupportedMethod => throw new UnsupportedOperationException(s"The method '$unsupportedMethod' is not supported. You can only use POST.")
      }

      val headers = req.headers.map(x => x.name -> x.value).toMap

      val content = req.entity.asString
      val body = if (content.nonEmpty) new Some(content) else None

      HttpRequestData(method, uri, headers = headers, body = body)
    }
  }
}