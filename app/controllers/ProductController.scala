package controllers

import models.Product
import play.api.Environment
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import java.time.LocalDateTime
import javax.inject.Inject

// Controller class for whenever a user enters the URL for the bakery.
class ProductController @Inject() (
                                    cc: ControllerComponents,
                                    environment: Environment
                                  ) extends AbstractController(cc) {
  // Create the format for the product in JSON form.
  implicit val productFormat = Json.format[Product]

  // When the user route to <root>/pass-bakery/status then this will return a status JSON
  def getAll = Action {

    // Get the current server time when the user enters the link
    val curDateTime = LocalDateTime.now()

    // Get the current environment
    val curEnv = environment.mode

    // Create a product object to pass it to JSON format
    val product = Product("pass-bakery", curEnv.toString, curDateTime.toString)

    // Send OK signal with JSON status
    Ok(Json.toJson(product))
  }
}
