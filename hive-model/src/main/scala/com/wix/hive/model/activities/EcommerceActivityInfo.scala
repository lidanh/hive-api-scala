package com.wix.hive.model.activities

import java.net.URL

import com.wix.hive.model.activities.ActivityType._
import com.wix.hive.model.activities.common.{Variant, Metadata, Media}


/**
 * Created by Uri_Keinan on 10/26/15.
 */
trait EcommerceActivityInfo extends ActivityInfo

case class EcommerceAbandon (cartId: String,
                             storeId: Int,
                             storeName: Option[String],
                             items: Seq[Item]) extends EcommerceActivityInfo {
  override val activityType = `e_commerce/cart-abandon`
}

case class EcommerceCartAddItem( cartId: String,
                                 storeId: Int,
                                 storeName: String,
                                 item: Item) extends EcommerceActivityInfo {
  override val activityType = `e_commerce/cart-add`
}

case class ECommercePurchase(
                              cartId: String,
                              storeId: String,
                              orderId: Option[String],
                              items: Seq[CartItem],
                              payment: Payment,
                              shippingAddress: Option[CartAddress],
                              billingAddress: Option[CartAddress],
                              paymentGateway: Option[String],
                              note: Option[String],
                              buyerAcceptsMarketing: Option[Boolean]
                              ) extends ActivityInfo {
  override val activityType = `e_commerce/purchase`
}

case class EcommerceCartCheckout( cartId: String,
                                  storeId: Int,
                                  storeName: Option[String],
                                  items: Seq[Item]
                                  ) extends EcommerceActivityInfo{
  override val activityType = `e_commerce/cart-checkout`
}

case class EcommerceCartRemoveItem( cartId: String,
                                    storeId: Int,
                                    storeName: String,
                                    item: Item) extends EcommerceActivityInfo {
  override val activityType = `e_commerce/cart-remove`

}




case class Item( id: String,
                          sku: Option[String],
                          title: String,
                          quantity: Int,
                          price: Option[java.math.BigDecimal],
                          formattedPrice: Option[String],
                          currency: String,
                          productLink: Option[URL],
                          weight: Option[java.math.BigDecimal],
                          formattedWeight: Option[String],
                          media: Option[Media],
                          variants: Seq[Variant],
                          `type`: Option[EcommerceItemType] = None,
                          categories: Option[Seq[String]] = None,
                          metadata: Option[Seq[Metadata]] = None)


case class EcommerceVariant(title: String, value: Option[String])

class EcommerceItemType

object EcommerceItemType extends Enumeration {
  type EcommerceItemType = Value
  val PHYSICAL, DIGITAL = Value
}


