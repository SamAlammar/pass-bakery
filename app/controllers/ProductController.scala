package controllers

import models.{EndPointStatus, Product}
import play.api.Environment
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.ProductDAO

import java.time.LocalDateTime
import javax.inject.Inject

/** Controller class for whenever a user enters the URL for the bakery.
 * @param cc
 * @param environment
 */
class ProductController @Inject() (
                                    cc: ControllerComponents,
                                    environment: Environment
                                  ) extends AbstractController(cc) with ProductDAO {

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
    val results : List[Product] = getResults[Product]

    Ok(Json.toJson(results.toString()))
  }
}
