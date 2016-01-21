package com.wix.hive.model.activities

import com.wix.hive.model.activities.ActivityType._
import org.joda.time.DateTime

case class HotelsReservation(source: Source,
                             reservationId: Option[String],
                             guests: Guests,
                             stay: Stay,
                             rates: Seq[Rate],
                             invoice: Invoice,
                             customer: Customer,
                             rooms: Seq[Room]) extends ActivityInfo {
  override def activityType: ActivityType = `hotels/reservation`
}

case class HotelsPurchase() extends ActivityInfo {
  override def activityType: ActivityType = `hotels/reservation`
}

case class Guests(total: Int, adults: Int, children: Int)

case class Stay(checkin: DateTime, checkout: DateTime)

case class Rate(date: DateTime, subtotal: Double, taxes: Seq[HotelTax], total: Double, currency: String)

case class HotelTax(name: String, total: Double, currency: String)

case class Invoice(subtotal: Double, total: Double, currency: String)

case class Customer(contactId: String, isGuest: Boolean, name: Name, phone: String, email: String)

case class Room(id: Option[String], beds: Seq[Bed], maxOccupancy: Double, amenities: Seq[String])

case class Bed(kind: String)