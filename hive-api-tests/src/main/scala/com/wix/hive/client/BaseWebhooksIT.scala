package com.wix.hive.client

import java.util.UUID

import com.twitter.finagle.{Http, Service}
import com.twitter.util.Duration
import com.wix.hive.client.infrastructure.WebhooksDriver
import com.wix.hive.server.FinagleWebhooksWebServer
import com.wix.hive.server.webhooks._
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}
import org.joda.time.DateTime
import org.mockito.Mockito._
import org.specs2.execute.AsResult._
import org.specs2.matcher.{AlwaysMatcher, Matcher}
import org.specs2.mock.Mockito
import org.specs2.specification.Before

import scala.util.Try

/**
 * User: maximn
 * Date: 12/2/14
 */
abstract class BaseWebhooksIT
  extends BaseIT
  with Mockito {
  self: WebhooksDriver =>

  val port: Int = 8001
  val path: String = "localhost/webhook-url/"
  val secret: String = "e5f5250a-dbd0-42a1-baf9-c61ea20c401b"

  override def initEnv(): Unit = srv.start()

  override def beforeTest(): Unit = reset(mockFunc)

  override def shutdownEnv(): Unit = srv.stop(Duration.fromMilliseconds(500))

  val mockFunc = mock[Try[Webhook[_]] => Unit]

  val key = "e5f5250a-dbd0-42a1-baf9-c61ea20c401b"

  val srv = new FinagleWebhooksWebServer(8001, key) {
    override def onReq(webhook: Try[Webhook[_]]): Unit = mockFunc(webhook)
  }

  step(initEnv())


  trait ctx extends Before
  with HiveCommandsMatchers {
    override def before: Any = beforeTest()

    val timeout = new org.specs2.time.Duration(2000)

    val client: Service[HttpRequest, HttpResponse] = Http.newService("localhost:8001")

    val appId = UUID.randomUUID().toString
    val instanceId = UUID.randomUUID().toString
    val timestamp = new DateTime(2014, 2, 11, 1, 2)

    def aWebhookParams(appId: String = appId, timestamp: DateTime = timestamp) = WebhookParameters(appId, timestamp)

    def aProvisionWebhook(instanceId: String = instanceId) = Webhook(instanceId, Provision(instanceId, None), aWebhookParams())

    def aProvisionDisabledWebhook(instanceId: String = instanceId) = Webhook(instanceId, ProvisionDisabled(instanceId, None), aWebhookParams())

    val activityId = "someActivityId"
    val activityType = "auth/register"

    def anActivityPostedWebhook(instanceId: String = instanceId) = Webhook(instanceId, ActivitiesPosted(activityId, activityType, None), aWebhookParams())
    def aServicesDoneWebhook(providerAppId: String = UUID.randomUUID().toString, instanceId: String = instanceId) = Webhook(instanceId, ServiceResult(providerAppId, "af142114-f616-4594-9fb8-1253d317541e", ServiceRunData("success", None, None)), aWebhookParams(appId))

    def anything[T] = AlwaysMatcher[T]()

    def beProvision(instanceId: Matcher[String], originInstanceId: Matcher[Option[String]] = beNone): Matcher[Provision] = {
      instanceId ^^ {(_: Provision).instanceId aka "instanceId"} and
        originInstanceId ^^ {(_: Provision).originInstanceId aka "originInstanceId"}
    }

    def beActivity(activityId: Matcher[String], activityType: Matcher[String], contactId: Matcher[Option[String]] = beNone): Matcher[ActivitiesPosted] = {
      activityId ^^ {(_: ActivitiesPosted).activityId aka "activityId"} and
        activityType ^^ {(_: ActivitiesPosted).activityType aka "activityType"} and
        contactId ^^ {(_: ActivitiesPosted).contactId aka "contactId"}
    }

    def beServiceRunResult(providerId: Matcher[String] = not(be(empty)),
                           correlationId: Matcher[String] = not(be(empty)),
                            data: Matcher[ServiceRunData] = anything): Matcher[ServiceResult] = {
      providerId ^^ {(_: ServiceResult).providerId aka "providerId"} and
      correlationId ^^ { (_: ServiceResult).correlationId aka "correlationId" } and
      data ^^ { (_: ServiceResult).data aka "data" }
    }

    def beProvisionDisabled(instanceId: Matcher[String], originInstanceId: Matcher[Option[String]] = beNone): Matcher[ProvisionDisabled] = {
      instanceId ^^ {(_: ProvisionDisabled).instanceId aka "instanceId"} and
        originInstanceId ^^ {(_: ProvisionDisabled).originInstanceId aka "originInstanceId"}
    }

    def beWebhook[T <: WebhookData](instanceId: Matcher[String], appId: Matcher[String], dataMatcher: Matcher[T]): Matcher[Webhook[T]] = {
      instanceId ^^ {(_: Webhook[T]).instanceId aka "instanceId"} and
        appId ^^ {(_: Webhook[T]).parameters.appId aka "parameters.appId"} and
        dataMatcher ^^ {(_: Webhook[T]).data aka "parameters.data"}
    }
  }


  "webhooks" should {
    "provision" in new ctx {
      callProvisionWebhook(aProvisionWebhook())
      there was after(timeout).one(mockFunc).apply(beSuccessfulTry[Webhook[Provision]].withValue(beWebhook(instanceId, appId, beProvision(instanceId, beNone))))
    }

    "provision disabled" in new ctx {
      callProvisionDisabledWebhook(aProvisionDisabledWebhook())

      there was after(timeout).one(mockFunc).apply(beSuccessfulTry[Webhook[ProvisionDisabled]].withValue(beWebhook(instanceId, appId, beProvisionDisabled(instanceId, beNone))))
    }

    "activity posted" in new ctx {
      callActivityPosted(appId, anActivityPostedWebhook())

      there was after(timeout).one(mockFunc).apply(beSuccessfulTry[Webhook[ActivitiesPosted]].withValue(beWebhook(instanceId, appId, beActivity(anything, activityType))))
    }

    "services done" in new ctx {
      val doneWebhook = aServicesDoneWebhook()
      callServicesDone(doneWebhook)
      there was after(timeout).one(mockFunc).apply(beSuccessfulTry[Webhook[ServiceResult]].withValue(beWebhook(instanceId, appId, beServiceRunResult())))
    }
  }

  step(shutdownEnv())
}