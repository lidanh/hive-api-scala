package com.wix.hive.model.activities

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.wix.hive.model.activities.ActivityType.{`social/comment`, `social/share-url`, `social/track`}
import com.wix.hive.model.activities.SocialChannel.SocialChannel
import com.wix.hive.model.activities.SocialType.SocialType
import com.wix.hive.model.activities.common.Metadata



/**
 * Created by Uri_Keinan on 10/20/15.
 */
trait SocialActivityInfo extends ActivityInfo {

}

case class SocialTrackActivityInfo(`type`: SocialType, tracker: Option[SocialTracker], metadata: Option[Metadata]) extends SocialActivityInfo {
  override val activityType = `social/track`
}

case class SocialCommentActivityInfo(text: String, @JsonScalaEnumeration(classOf[SocialChannelRef])channel: Option[SocialChannel], metadata: Option[Seq[Metadata]]) extends SocialActivityInfo {
  override val activityType = `social/comment`
}

case class SocialShareUrlActivityInfo(url: String, text: Option[String], @JsonScalaEnumeration(classOf[SocialChannelRef])channel: Option[SocialChannel], sharer: Option[Name], metadata:Option[Seq[Metadata]]) extends SocialActivityInfo {
  override val activityType = `social/share-url`
}


object SocialType extends Enumeration {
  type SocialType = Value
  val LIKE, FOLLOW, SUBSCRIBE, PIN_IT, FAVORITE, OTHER = Value
}

case class SocialTracker(@JsonScalaEnumeration(classOf[SocialChannelRef])openId: Option[SocialChannel], name: Option[Name], email: Option[String])

class SocialChannelRef extends TypeReference[SocialChannel.type]

object SocialChannel extends Enumeration {
  type SocialChannel = Value
 val FACEBOOK, TWITTER, LINKEDIN, GOOGLE_PLUS, PINTEREST, TUMBLR, BLOGGER, WORDPRESS, OTHER = Value
}
