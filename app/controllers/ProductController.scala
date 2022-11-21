package controllers

import models.{EndPointStatus, Products}
import org.postgresql.util.PSQLException
import play.api.Environment
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Json.toJson
import play.api.libs.json.{JsError, JsPath, JsValue, Json, Reads}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.ProductDAO

import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/** Controller class for whenever a user enters the URL for the bakery.
 * @param cc
 * @param environment
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

    Ok(Json.toJson(Json.toJson(results)))
  }

  /** When the user GET to <root>/rest/bakery/product/:id where id is the UUID for a product,
   * make sure it's a valid UUID string (or an exception will get thrown) then check if it found
   * a result or not (result is empty) and return the appropriate status + body if needed.
   * @param id
   * @return
   */
  def getProduct(id: String): Action[AnyContent] = Action {

    // Used variable isError to make sure I don't overload the method by giving multiple status
    var isError = false
    var result: List[Products] = List.empty

    // Strings not being correct UUID format will throw an exception, so this will catch it
    try
      result = getResultById[Products](id)
    catch {
      case x: PSQLException =>
        isError = true
    }

    // I have to make sure I don't keep giving multiple statuses so this if-else will resolve it
    if (result.isEmpty && !isError)
      NotFound("ID not found")
    else if(isError)
      NotFound("Wrong ID string")
    else
      Ok(Json.toJson(Json.toJson(result)))
  }

  /** When the user POST to <root>/rest/bakery/product with a JSON request then this
   *  will insert the given JSON into the database.
   * @return
   */
  def addProduct(): Action[JsValue] = Action(parse.json) { request =>

    val productResult = request.body.validate[List[Products]]
    productResult.fold(
      errors => {
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
   * @param id
   * @return
   */
  def updateProduct(id: String): Action[JsValue] = Action(parse.json) { request =>

    val productResult = request.body.validate[List[Products]]
    productResult.fold(
      errors => {
        BadRequest("Something went wrong, this error shouldn't exist, debug it further to find out")
      },
      product => {
        var isError = false
        var result = 0

        // Strings not being correct UUID format will throw an exception, so this will catch it
        try
          result = updateResultById[Products](id, product.head)
        catch {
          case x: PSQLException =>
            isError = true
        }

        // I have to make sure I don't keep giving multiple statuses so this if-else will resolve it
        if ((result == 0) && !isError)
          NotFound("ID not found")
        else if (isError)
          NotFound("Wrong ID string")
        else
          Ok(Json.obj("message" -> "Product updated."))
      }
    )
  }

  /** When the user DELETE to <root>/rest/bakery/product/:id where id is the UUID for a product,
   * make sure it's a valid UUID string (or an exception will get thrown) then check if it found
   * a result or not (result is empty) and return the appropriate status.
   *
   * @param id
   * @return
   */
  def deleteProduct(id: String): Action[AnyContent] = Action {

    // Used variable isError to make sure I don't overload the method by giving multiple status
    var isError = false
    var result = 0

    // Strings not being correct UUID format will throw an exception, so this will catch it
    try
      result = deleteResultById[Products](id)
    catch {
      case x: PSQLException =>
        isError = true
    }

    // I have to make sure I don't keep giving multiple statuses so this if-else will resolve it
    if ((result == 0) && !isError)
      NotFound("ID not found")
    else if (isError)
      NotFound("Wrong ID string")
    else
      Ok(Json.obj("message" -> "Product deleted."))
  }
}
