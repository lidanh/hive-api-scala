package com.wix.hive.model.activities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.wix.hive.model.activities.ActivityType._

import com.wix.hive.model.activities.Direction.Direction

case class MessagesEmail(content: Content,
                         messageId: String,
                         threadId: String,
                         linkedMessageId: Option[String],
                         to: Seq[String],
                         cc: Seq[String],
                         subject: Option[String],
                         replyTo: Option[String],
                         @JsonScalaEnumeration(classOf[DirectionRef])direction: Direction,
                         metadata: Seq[ItemMetadata]) extends ActivityInfo {
  @JsonIgnore
  override def activityType: ActivityType = `messaging/email`
}

case class Content(message: String, media: Seq[String])

class DirectionRef extends TypeReference[Direction.type]

object Direction extends Enumeration {
  type Direction = Value
  val BusinessToCustomer = Value("BUSINESS_TO_CUSTOMER")
  val CustomerToBusiness = Value("CUSTOMER_TO_BUSINESS")
}
