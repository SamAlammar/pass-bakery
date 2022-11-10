package models

import play.api.db.{Database, NamedDatabase}
import play.api.libs.json.{JsMacroImpl, Json, OFormat}

import javax.inject.Inject
import scala.concurrent.Future
import scala.language.experimental.macros

trait ImplicitJsonFormat
trait JsonImplicits {
  implicit def implicitJsonFormat[A <: ImplicitJsonFormat]: OFormat[A] = macro JsMacroImpl.implicitConfigFormatImpl[A]
}
case class Product(ID: String, name: String,quantity: Option[String],price: Option[String],
                   createdAt: Option[String],updatedAt: Option[String])
case class EndPointStatus(service: String, environment: String, serverTime: String)

object EndPointStatus extends JsonImplicits {
  implicit val format: OFormat[EndPointStatus] = Json.format
}
