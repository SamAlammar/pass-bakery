package controllers

import doobie.implicits._
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.Transactor
import doobie.implicits.toSqlInterpolator
import models.Product
import play.api.Environment
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import java.time.LocalDateTime
import javax.inject.Inject

/** Controller class for whenever a user enters the URL for the bakery.
 * @param cc
 * @param environment
 */
class ProductController @Inject() (
                                    cc: ControllerComponents,
                                    environment: Environment
                                  ) extends AbstractController(cc) {
  // Create the format for the product in JSON form.
  implicit val productFormat: OFormat[Product] = Json.format[Product]

  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql:bakery-inventory", // connect URL (driver-specific)
    "postgres", // user
    "" // password
  )

  // When the user route to <root>/pass-bakery/status then this will return a status JSON
  def getStatus: Action[AnyContent] = Action {

    // Used LocalDateTime since it time and date is formatted in ISO-8601 as it is written in its doc.
    val curDateTime = LocalDateTime.now()

    val curEnv = environment.mode
    val product = Product("pass-bakery", curEnv.toString, curDateTime.toString)

    // Send OK signal with JSON status
    Ok(Json.toJson(product))
  }

  // When the user route to <root>/rest/bakery/products then this will return all the elements of the bakery
  // database.
  def getInventory: Action[AnyContent] = Action {

    // Select * statement with rows returned as a list so I can put it in JSON format
    val result = sql"select * from bakery_table"
      .query[(String, String, Option[String], Option[String], Option[String], Option[String])] // Query0[*]
      .to[List] // ConnectionIO[List[String]]
      .transact(xa) // IO[List[String]]
      .unsafeRunSync() // List[String]
      .take(5) // List[String]

    Ok(Json.toJson(result))
  }
}
