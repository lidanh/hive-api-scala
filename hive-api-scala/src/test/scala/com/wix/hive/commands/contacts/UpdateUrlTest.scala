package com.wix.hive.commands.contacts

import com.wix.hive.client.http.HttpMethod
import com.wix.hive.matchers.HiveMatchers
import com.wix.hive.model.contacts.EmailStatus
import org.specs2.mutable.{SpecificationWithJUnit, Specification}

class UpdateUrlTest extends SpecificationWithJUnit with HiveMatchers {

  class Context extends ContextForModification {
    val urlId = "027206f3-e278-43a7-be27-93610cbf25a0"
    val url = ContactUrlDTO("tag-url", "http://some.wix.com/site")

    val cmd = UpdateUrl(contactId, modifiedAt, urlId, url)
  }

  "createHttpRequestData" should {
    "work with parameters" in new Context {
      cmd.createHttpRequestData must httpRequestDataWith(
        method = be_===(HttpMethod.PUT),
        url = contain(contactId) and contain("url") and contain(urlId),
        query = havePair("modifiedAt", modifiedAt.toString),
        body = beSome(be_==(url))
      )
    }
  }
}
