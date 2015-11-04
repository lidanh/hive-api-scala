package com.wix.hive.commands.labels

import com.wix.hive.client.http.HttpMethod
import com.wix.hive.matchers.HiveMatchers
import org.joda.time.DateTime
import org.specs2.mutable._
import org.specs2.specification.Scope

class UpdateLabelNameTest extends SpecificationWithJUnit with HiveMatchers {

  class Context extends Scope {
    val id = "label-id"
    val command = UpdateLabelName(id, "Label name", new DateTime(2010, 1, 1, 0, 0))
  }

  "createHttpRequestData" should {
    "create correct HttpRequestData" in new Context {
      command.createHttpRequestData must httpRequestDataWith(
        method = be_===(HttpMethod.PUT),
        url = be_===(s"/labels/$id/name"),
        body = beSome(UpdateLabelNameData(command.name)),
        query = havePairs(
          "modifiedAt" -> command.modifiedAt.toString) and haveSize(1)
      )
    }
  }
}
