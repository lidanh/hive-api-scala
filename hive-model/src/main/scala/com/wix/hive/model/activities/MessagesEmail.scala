package com.wix.hive.model.activities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wix.hive.model.activities.ActivityType._

case class MessagesEmail(content: Content,
                         messageId: String,
                         threadId: String,
                         linkedMessageId: Option[String],
                         to: Seq[String],
                         cc: Seq[String],
                         subject: Option[String],
                         replyTo: Option[String],
                         direction: Enumeration,
                         metadata: Seq[ItemMetadata]) extends ActivityInfo {
  @JsonIgnore
  override def activityType: ActivityType = `messaging/email`
}

case class Content(message: String, media: Seq[String])
