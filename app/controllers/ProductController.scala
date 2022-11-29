package controllers

import models.{EndPointStatus, Products}
import models.UserProducts
import play.api.Environment
import play.api.libs.json.Json
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
   * @return JSON of the status of current endpoint
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
   * @return A list JSON containing all the products in the database.
   */
  def getInventory: Action[AnyContent] = Action {
    val results : List[Products] = getResults[Products]

    Ok(Json.toJson(results))
  }

  /** When the user GET to <root>/rest/bakery/product/:id where id is the UUID for a product,
   * make sure it's a valid UUID string (or an exception will get thrown) then check if it found
   * a result or not (result is empty) and return the appropriate status + body if needed.
   * @param id: String of the UUID given by the user in the URL
   * @return A 200 signal with the product pulled from database or 404 saying the ID is incorrect
   */
  def getProduct(id: String): Action[AnyContent] = Action {
    val result: List[Products] = getResultById[Products](id)

    if (result.isEmpty)
      NotFound("Incorrect ID, check the ID again if it is wrong format or none existant")
    else
      Ok(Json.toJson(result.head))
  }

  /** When the user POST to <root>/rest/bakery/product with a JSON request then this
   *  will insert the given JSON into the database.
   *
   * Request Body: A JSON with the new name, quantity, and price of the product being added
   * @return A signal saying product is added
   */
  def addProduct(): Action[UserProducts] = Action(parse.json[UserProducts]) { request =>
    save(request.body)

    Ok(Json.obj("message" -> "Product saved."))
  }

  /** When the user PUT to <root>/rest/bakery/product/:id with a JSON request
   * and the id is the UUID for a product, make sure it's a valid UUID string
   * (or an exception will get thrown) then check if it found a result
   * or not (result is zero) and return the appropriate status.
   *
   * @param id: String of the UUID given by the user in the URL
   * Request Body: A JSON with the new name, quantity, and price of the product being updated
   * @return A status signal, either 200 with product updated or 404 with incorrect ID
   */
  def updateProduct(id: String): Action[UserProducts] = Action(parse.json[UserProducts]) { request =>
      val result = updateResultById[Products](id, request.body)

      result match {
        case 0 => NotFound ("ID not found")
        case -1 => NotFound ("Wrong ID string")
        case _ => Ok (Json.obj ("message" -> "Product updated.") )
      }
  }

  /** When the user DELETE to <root>/rest/bakery/product/:id where id is the UUID for a product,
   * make sure it's a valid UUID string (or an exception will get thrown) then check if it found
   * a result or not (result is empty) and return the appropriate status.
   *
   * @param id: String of the UUID given by the user in the URL
   * @return A 200 signal with product deleted or 404 signal due to incorrect ID given by user
   */
  def deleteProduct(id: String): Action[AnyContent] = Action {
    val result = deleteResultById[Products](id)

    result match {
      case 0 => NotFound("ID not found")
      case -1 => NotFound("Wrong ID string")
      case _ => Ok(Json.obj("message" -> "Product deleted."))
    }
  }
}
