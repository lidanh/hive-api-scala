package com.wix.hive.commands.labels

import com.wix.hive.client.http.HttpMethod
import com.wix.hive.matchers.HiveMatchers
import org.specs2.mutable._
import org.specs2.specification.Scope

class CreateLabelTest extends SpecificationWithJUnit with HiveMatchers {

  class Context extends Scope {
    val command = CreateLabel("Label name", Some("Description"))
  }

  "createHttpRequestData" should {
    "create correct HttpRequestData" in new Context {
      command.createHttpRequestData must httpRequestDataWith(
        method = be_===(HttpMethod.POST),
        url = be_===("/labels"),
        body = beSome(CreateLabelData(command.name, command.description))
      )
    }
  }
}
