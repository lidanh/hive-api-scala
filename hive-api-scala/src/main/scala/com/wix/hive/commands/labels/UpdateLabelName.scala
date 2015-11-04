package com.wix.hive.commands.labels

import com.wix.hive.client.http.HttpMethod
import com.wix.hive.client.http._
import com.wix.hive.client.http.HttpMethod._
import com.wix.hive.model.labels.Label
import org.joda.time.DateTime

case class UpdateLabelName(id: String, name: String, modifiedAt: DateTime) extends LabelsCommand[Label] {
  override def method: HttpMethod = HttpMethod.PUT

  override def body = Some(UpdateLabelNameData(name))

  override def urlParams = super.urlParams + s"/$id/name"

  override def query: NamedParameters = super.query + ("modifiedAt" -> modifiedAt.toString)
}

case class UpdateLabelNameData(name: String)