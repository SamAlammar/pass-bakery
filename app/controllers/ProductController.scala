package controllers

import models.{EndPointStatus, Products}
import play.api.Environment
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.ProductDAO

import java.time.LocalDateTime
import javax.inject.Inject

/** Controller class for whenever a user enters the URL for the bakery.
 * @param cc: The base controller components dependencies that most controllers rely on.
 * @param environment: The environment for the application.
 */
class ProductController @Inject() (
                                    cc: ControllerComponents,
                                    environment: Environment
                                  ) extends AbstractController(cc) with ProductDAO {

  /**
   * When the user route to <root>/pass-bakery/status then this will return a status JSON
   * @return
   */
  def getStatus: Action[AnyContent] = Action {

    // Used LocalDateTime since it time and date is formatted in ISO-8601 as it is written in its doc.
    val curDateTime = LocalDateTime.now()

    val curEnv = environment.mode
    val status = EndPointStatus("pass-bakery", curEnv.toString, curDateTime.toString)

    Ok(Json.toJson(status))
  }

  /** When the user route to <root>/rest/bakery/products then this will return all
   *  the elements of the product database.
   * @return
   */
  def getInventory: Action[AnyContent] = Action {
    val results : List[Products] = getResults[Products]

    Ok(Json.toJson(results))
  }

  /** When the user GET to <root>/rest/bakery/product/:id where id is the UUID for a product,
   * make sure it's a valid UUID string (or an exception will get thrown) then check if it found
   * a result or not (result is empty) and return the appropriate status + body if needed.
   * @param id: String of the UUID given by the user in the URL
   * @return
   */
  def getProduct(id: String): Action[AnyContent] = Action {
    val result: List[Products] = getResultById[Products](id)

    // I have to make sure I don't keep giving multiple statuses so this if-else will resolve it
    if (result == null)
      NotFound("Wrong ID string")
    else if (result.isEmpty)
      NotFound("ID not found")
    else
      Ok(Json.toJson(result))
  }

  /** When the user POST to <root>/rest/bakery/product with a JSON request then this
   *  will insert the given JSON into the database.
   * @return
   */
  def addProduct(): Action[JsValue] = Action(parse.json) { request =>

    val productResult = request.body.validate[List[Products]]
    productResult.fold(
      _ => {
        BadRequest("Something went wrong, this error shouldn't exist, debug it further to find out")
      },
      product => {
        save(product.head)
        Ok(Json.obj("message" -> "Product saved."))
      }
    )
  }

  /** When the user PUT to <root>/rest/bakery/product/:id with a JSON request
   * and the id is the UUID for a product, make sure it's a valid UUID string
   * (or an exception will get thrown) then check if it found a result
   * or not (result is zero) and return the appropriate status.
   *
   * @param id: String of the UUID given by the user in the URL
   * @return
   */
  def updateProduct(id: String): Action[JsValue] = Action(parse.json) { request =>

    val productResult = request.body.validate[List[Products]]
    productResult.fold(
      _ => {
        BadRequest("Something went wrong, this error shouldn't exist, debug it further to find out")
      },
      product => {
        val result = updateResultById[Products](id, product.head)

        // I have to make sure I don't keep giving multiple statuses so this if-else will resolve it
        result match {
          case 0 => NotFound ("ID not found")
          case -1 => NotFound ("Wrong ID string")
          case _ => Ok (Json.obj ("message" -> "Product updated.") )
        }
      }
    )
  }

  /** When the user DELETE to <root>/rest/bakery/product/:id where id is the UUID for a product,
   * make sure it's a valid UUID string (or an exception will get thrown) then check if it found
   * a result or not (result is empty) and return the appropriate status.
   *
   * @param id: String of the UUID given by the user in the URL
   * @return
   */
  def deleteProduct(id: String): Action[AnyContent] = Action {
    val result = deleteResultById[Products](id)

    // I have to make sure I don't keep giving multiple statuses so this if-else will resolve it
    result match {
      case 0 => NotFound("ID not found")
      case -1 => NotFound("Wrong ID string")
      case _ => Ok(Json.obj("message" -> "Product deleted."))
    }
  }
}
