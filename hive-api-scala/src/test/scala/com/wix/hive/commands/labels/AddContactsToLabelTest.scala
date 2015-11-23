package com.wix.hive.commands.labels

import com.wix.hive.commands.labels.ContactDataType.CONTACTS
import com.wix.hive.json.JacksonObjectMapper.mapper
import com.wix.hive.matchers.HiveMatchers
import org.specs2.matcher.JsonMatchers
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.Scope


class AddContactsToLabelTest extends SpecWithJUnit with JsonMatchers with HiveMatchers {

  class Context extends Scope {
    val id = "label-id"
    val contactId = "contact-id"
    val command = new AddContactsToLabel(id = id, dataType = CONTACTS, ids = Seq(contactId))
  }

  "createHttpRequestData" should {
    "serialize to correct body" in new Context {
      mapper.writeValueAsString(command.body.get) must (
        /("dataType" -> "CONTACTS")
          and /("data") /# 0 / "contact-id"
        )
    }
  }
}
