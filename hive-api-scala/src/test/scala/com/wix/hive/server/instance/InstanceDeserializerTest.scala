package com.wix.hive.server.instance

import org.specs2.matcher.MatcherMacros
import org.specs2.mutable.Specification

import scala.language.experimental.macros

/**
 * User: maximn
 * Date: 7/13/15
 */
class InstanceDeserializerTest extends Specification with MatcherMacros {

  class ctx extends InstanceDecoderScope {
    val deserializer = new InstanceDeserializer
  }

  "WixInstance deserialization" should {
    "handle permissions `null`" in new ctx {
      val inst = generateInstance(permissions = "null").getBytes
      deserializer.deserialize(inst) must matchA[WixInstance].permissions(empty)
    }

    "handle userId is `null`" in new ctx {
      val inst = generateInstance(userId = None).getBytes
      deserializer.deserialize(inst) must matchA[WixInstance].userId(beNone)
    }

    "handle premiumPackage is `null`" in new ctx {
      val inst = generateInstance(premiumPackageId = None).getBytes
      deserializer.deserialize(inst) must matchA[WixInstance].premiumPackageId(beNone)
    }
  }
}
