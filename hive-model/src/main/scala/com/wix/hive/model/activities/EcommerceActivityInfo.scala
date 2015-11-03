package com.wix.hive.model.activities

import java.net.URL

import com.wix.hive.model.activities.ActivityType._
import com.wix.hive.model.activities.common.Metadata


/**
 * Created by Uri_Keinan on 10/26/15.
 */
trait EcommerceActivityInfo extends ActivityInfo

case class ECommerceAbandon (cartId: Option[String],
                             storeId: Option[Int],
                             storeName: Option[String],
                             items: Seq[Item]) extends EcommerceActivityInfo {
  override val activityType = `e_commerce/cart-abandon`
}

case class ECommerceCartAddItem( cartId: String,
                                 storeId: Int,
                                 storeName: String,
                                 item: Item) extends EcommerceActivityInfo {
  override val activityType = `e_commerce/cart-add`
}

case class ECommercePurchase(
                              cartId: Option[String],
                              storeId: Option[String],
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

case class ECommerceCartCheckout( cartId: Option[String],
                                  storeId: Option[Int],
                                  storeName: Option[String],
                                  items: Seq[Item]
                                  ) extends EcommerceActivityInfo{
  override val activityType = `e_commerce/cart-checkout`
}

case class ECommerceCartRemoveItem( cartId: Option[String],
                                    storeId: Option[Int],
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


