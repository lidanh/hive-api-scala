package com.wix.hive.commands.labels

import com.wix.hive.client.http.HttpMethod
import com.wix.hive.client.http.HttpMethod._

case class CreateLabel(name: String, description: Option[String]) extends LabelsCommand[CreatedLabel] {
  override def method: HttpMethod = HttpMethod.POST

  override def body = Some(CreateLabelData(name, description))
}

case class CreateLabelData(name: String, description: Option[String])
