package com.wix.hive.commands

import com.wix.hive.drivers.HiveCommandsMatchers._
import com.wix.hive.drivers.LabelsTestSupport
import com.wix.hive.infrastructure.HiveSimplicatorIT
import com.wix.hive.model.labels.PagingLabelsResult

/**
 * User: karenc
 * Date: 2/1/15
 */
class LabelsIT extends HiveSimplicatorIT {


  class ClientContext extends HiveClientContext with LabelsTestSupport

  "Labels operations" should {

    "get label by id" in new ClientContext {
      expect(app, getLabelByIdCommand)(label)

      client.execute(instance, getLabelByIdCommand) must beLabelWithId(labelId).await
    }

    "get labels with filtering" in new ClientContext {
      expect(app, getLabelsCommand)(PagingLabelsResult(total = 2, pageSize = 25, previousCursor = None, nextCursor = None, results = Seq(label, anotherLabel)))

      client.execute(instance, getLabelsCommand) must beLabelsWith(contain(allOf(beLabelWithId(labelId), beLabelWithId(anotherLabelId)))).await
    }

    "create a label" in new ClientContext {
      expect(app, createLabelCommand)(createdLabel)
      client.execute(instance, createLabelCommand) must beCreatedLabelWithId(labelId).await
    }

    "change the label name" in new ClientContext {
      expect(app, updateLabelNameCommand)(label)
      client.execute(instance, updateLabelNameCommand) must beLabelWithId(labelId).await
    }

    "add a contact to the label" in new ClientContext {
      expect(app, addLabelContactsCommand)(affectedContacts)
      client.execute(instance, addLabelContactsCommand) must haveAffectedContacts(be_==(1)).await
    }
  }
}