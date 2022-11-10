package controllers

import doobie.implicits._
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import doobie.{Fragment, Read, Transactor}
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor.Aux
import models.{EndPointStatus, Product}
import play.api.ApplicationLoader.Context
import play.api.{BuiltInComponentsFromContext, Environment}
import play.api.db.{DBComponents, Database, HikariCPComponents}
import play.api.db.evolutions.EvolutionsComponents
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.api.routing.Router
import play.filters.HttpFiltersComponents

import java.time.LocalDateTime
import javax.inject.Inject

/** Controller class for whenever a user enters the URL for the bakery.
 * @param cc
 * @param environment
 */
class ProductController @Inject() (
                                    cc: ControllerComponents,
                                    environment: Environment,
                                  ) extends AbstractController(cc) {


  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql:bakery_db", // connect URL (driver-specific)
    "postgres", // user
    "" // password
  )

  // When the user route to <root>/pass-bakery/status then this will return a status JSON
  def getStatus: Action[AnyContent] = Action {

    // Used LocalDateTime since it time and date is formatted in ISO-8601 as it is written in its doc.
    val curDateTime = LocalDateTime.now()

    val curEnv = environment.mode
    val status = EndPointStatus("pass-bakery", curEnv.toString, curDateTime.toString)

    // Send OK signal with JSON status
    Ok(Json.toJson(status))
  }

  // When the user route to <root>/rest/bakery/products then this will return all the elements of the bakery
  // database.
  def getInventory: Action[AnyContent] = Action {

    // Select * statement with rows returned as a list so I can put it in JSON format
    def getResults[T: Read]: List[T] = {
        sql"select * from Bakery".query[T] // Query0[*]
          .to[List] // ConnectionIO[List[Product]]
          .transact(xa) // IO[List[Product]]
          .unsafeRunSync() // List[Product]
          .take(5) // List[Product]
    }

    val results : List[Product] = getResults[Product]


    Ok(Json.toJson(results.toString()))
  }
}
