package com.wix.hive.model.activities

import com.fasterxml.jackson.core.`type`.TypeReference

class ActivityTypeRef extends TypeReference[ActivityType.type]

object ActivityType extends Enumeration {
  type ActivityType = Value
  val `auth/login` = Value("auth/login")
  val `auth/register` = Value("auth/register")
  val `auth/status-change` = Value("auth/status-change")
  val `contact/contact-form` = Value("contact/contact-form")
  val `contact/subscription-form` = Value("contact/subscription-form")
  val `e_commerce/cart-abandon` = Value("e_commerce/cart-abandon")
  val `e_commerce/cart-add` = Value("e_commerce/cart-add")
  val `e_commerce/cart-checkout` = Value("e_commerce/cart-checkout")
  val `e_commerce/purchase` = Value("e_commerce/purchase")
  val `e_commerce/cart-remove` = Value("e_commerce/cart-remove")
  val `music/album-fan` = Value("music/album-fan")
  val `music/album-share` = Value("music/album-share")
  val `music/track-lyrics` = Value("music/track-lyrics")
  val `music/track-play` = Value("music/track-play")
  val `music/track-played` = Value("music/track-played")
  val `music/track-share` = Value("music/track-share")
  val `music/track-skip` = Value("music/track-skip")
  val `scheduler/appointment` = Value("scheduler/appointment")
  val `scheduler/cancel` = Value("scheduler/cancel")
  val `scheduler/confirmation` = Value("scheduler/confirmation")
  val `social/comment` = Value("social/comment")
  val `social/share-url` = Value("social/share-url")
  val `social/track` = Value("social/track")
  val `hotels/purchase` = Value("hotels/purchase")
  val `hotels/reservation` = Value("hotels/reservation")
  val `messaging/email` = Value("messaging/email")
}
