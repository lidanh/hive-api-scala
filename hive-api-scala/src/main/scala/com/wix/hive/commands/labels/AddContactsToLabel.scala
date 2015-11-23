package com.wix.hive.commands.labels

import com.wix.hive.client.http.HttpMethod
import com.wix.hive.client.http.HttpMethod._
import com.wix.hive.model.labels.AddContactsToLabelResult

case class AddContactsToLabel(id: String, dataType: ContactDataType, ids: Seq[String]) extends LabelsCommand[AddContactsToLabelResult] {
  override def method: HttpMethod = HttpMethod.POST

  override def body = Some(LabelOperationData(dataType, ids))

  override def urlParams = super.urlParams + s"/$id/contacts"
}

case class LabelOperationData(dataType: ContactDataType, data: Seq[String])
