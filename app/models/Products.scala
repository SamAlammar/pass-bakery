package models

import play.api.libs.json.{JsMacroImpl, Json, OFormat}
import scala.language.experimental.macros

trait ImplicitJsonFormat
trait JsonImplicits {
  implicit def implicitJsonFormat[A <: ImplicitJsonFormat]: OFormat[A] = macro JsMacroImpl.implicitConfigFormatImpl[A]
}

case class EndPointStatus(service: String,
                          environment: String,
                          serverTime: String)

object EndPointStatus extends JsonImplicits {
  implicit val format: OFormat[EndPointStatus] = Json.format
}

case class Products(id: String,
                    name: String,
                    quantity: Option[Int],
                    price: Option[Double],
                    createdAt: Option[String],
                    updatedAt: Option[String])

object Products {
  implicit val format: OFormat[Products] = Json.format
}
