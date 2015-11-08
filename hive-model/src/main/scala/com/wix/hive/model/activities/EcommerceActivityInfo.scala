package com.wix.hive.model.activities

import com.wix.hive.model.activities.ActivityType._


/**
 * Created by Uri_Keinan on 10/26/15.
 */
trait EcommerceActivityInfo extends ActivityInfo

case class ECommerceAbandon (cartId: Option[String],
                             storeId: Option[Int],
                             storeName: Option[String],
                             items: Seq[CartItem]) extends EcommerceActivityInfo {
  override val activityType = `e_commerce/cart-abandon`
}

case class ECommerceCartAddItem( cartId: Option[String],
                                 storeId: Option[Int],
                                 storeName: String,
                                 item: CartItem) extends EcommerceActivityInfo {
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

//for activity change purposes will be deleted after the change

case class ECommercePurchaseWithTemp(
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
                                  items: Seq[CartItem]
                                  ) extends EcommerceActivityInfo{
  override val activityType = `e_commerce/cart-checkout`
}

case class ECommerceCartRemoveItem( cartId: Option[String],
                                    storeId: Option[Int],
                                    storeName: String,
                                    item: CartItem) extends EcommerceActivityInfo {
  override val activityType = `e_commerce/cart-remove`

}







case class EcommerceVariant(title: String, value: Option[String])

class EcommerceItemType

object EcommerceItemType extends Enumeration {
  type EcommerceItemType = Value
  val PHYSICAL, DIGITAL = Value
}


