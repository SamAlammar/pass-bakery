package models

import play.api.libs.json.{JsMacroImpl, Json, OFormat}

import scala.language.experimental.macros

trait ImplicitJsonFormat
trait JsonImplicits {
  implicit def implicitJsonFormat[A <: ImplicitJsonFormat]: OFormat[A] = macro JsMacroImpl.implicitConfigFormatImpl[A]
}

case class Product(service: String, environment: String, serverTime: String)

object Product extends JsonImplicits {
  implicit val format: OFormat[Product] = Json.format
}
