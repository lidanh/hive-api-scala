package com.wix.hive.commands.labels

import com.wix.hive.client.http.HttpMethod
import com.wix.hive.client.http.HttpMethod._

case class AddLabelContacts(id: String, dataType: ContactDataType, ids: Seq[String]) extends LabelsCommand[AffectedContacts] {
  override def method: HttpMethod = HttpMethod.POST

  override def body = Some(AddLabelContactsData(dataType, ids))

  override def urlParams = super.urlParams + s"/$id/contacts"
}


case class AddLabelContactsData(dataType: ContactDataType, data: Seq[String])
