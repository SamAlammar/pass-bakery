package models

import play.api.libs.json.{Json, OFormat}

case class UserProducts(name: String,
                        quantity: Option[Int],
                        price: Option[Double])

object UserProducts {
  implicit val format: OFormat[UserProducts] = Json.format[UserProducts]
}
