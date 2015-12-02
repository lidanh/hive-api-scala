package com.wix.hive.model.activities

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.wix.hive.model.activities.ActivityType.{`social/comment`, `social/share-url`, `social/track`}
import com.wix.hive.model.activities.SocialChannel.SocialChannel
import com.wix.hive.model.activities.SocialChannelWithSite.SocialChannelWithSite
import com.wix.hive.model.activities.SocialType.SocialType
import com.wix.hive.model.activities.common.Metadata



/**
 * Created by Uri_Keinan on 10/20/15.
 */
trait SocialActivityInfo extends ActivityInfo {

}

case class SocialTrackActivityInfo(@JsonScalaEnumeration(classOf[SocialTypeRef])`type`: SocialType, tracker: Option[SocialTracker], metadata: Seq[Metadata]) extends SocialActivityInfo {
  override val activityType = `social/track`
}

case class SocialCommentActivityInfo(text: String, @JsonScalaEnumeration(classOf[SocialChannelWithSiteRef])channel: Option[SocialChannelWithSite], metadata: Seq[Metadata], commenter: Option[SocialTracker]) extends SocialActivityInfo {
  override val activityType = `social/comment`
}

case class SocialShareUrlActivityInfo(url: String, text: Option[String], @JsonScalaEnumeration(classOf[SocialChannelRef])channel: SocialChannel, sharer: Option[SocialSharer], metadata:Seq[Metadata]) extends SocialActivityInfo {
  override val activityType = `social/share-url`
}

case class SocialOpenId(@JsonScalaEnumeration(classOf[SocialChannelRef])channel: SocialChannel)

class SocialTypeRef extends TypeReference[SocialType.type]

object SocialType extends Enumeration {
  type SocialType = Value
  val LIKE, FOLLOW, SUBSCRIBE, PIN_IT, FAVORITE, OTHER = Value
}

case class SocialSharer (name: Option[Name], email: Option[String])

case class SocialTracker(openId: Option[SocialOpenId], name: Option[Name], email: Option[String])

class SocialChannelRef extends TypeReference[SocialChannel.type]

object SocialChannel extends Enumeration {
  type SocialChannel = Value
 val FACEBOOK, TWITTER, LINKEDIN, GOOGLE_PLUS, PINTEREST, INSTAGRAM, TUMBLR, BLOGGER, WORDPRESS, OTHER = Value
}

class SocialChannelWithSiteRef extends TypeReference[SocialChannelWithSite.type]

object SocialChannelWithSite extends Enumeration {
    type SocialChannelWithSite = Value
    val SITE, FACEBOOK, TWITTER, LINKEDIN, GOOGLE_PLUS, PINTEREST, INSTAGRAM, TUMBLR, BLOGGER, WORDPRESS, OTHER = Value
}