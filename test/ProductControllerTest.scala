import akka.stream.Materializer
import controllers.ProductController

import scala.concurrent.Future
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Environment
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

/** A test class for ProductController to see whether it returns the correct signal
 *
 */
class ProductControllerTest extends PlaySpec
  with Results
  with GuiceOneAppPerSuite {

  "<root>/pass-bakery/status test" should {
    "be valid" in {
      val environment = Environment.simple()

      val controller             = new ProductController(Helpers.stubControllerComponents(), environment)
      val result: Future[Result] = controller.getStatus().apply(FakeRequest())

      status(result) mustBe 200
    }
  }

  "GET <root>/rest/bakery/products test" should {
    "be valid" in {
      val environment = Environment.simple()

      val controller = new ProductController(Helpers.stubControllerComponents(), environment)
      val result: Future[Result] = controller.getInventory().apply(FakeRequest())

      status(result) mustBe 200
    }
  }

  "GET <root>/rest/bakery/product/:id" should{
    val environment = Environment.simple()

    val controller = new ProductController(Helpers.stubControllerComponents(), environment)
    val result: Future[Result] = controller.getInventory().apply(FakeRequest())
    val json: JsValue = Json.parse(contentAsString(result))

    val existingId = (json.as[Seq[JsObject]].head \ "id").as[String]
    val noneId = existingId.replace(existingId.charAt(0), (existingId.charAt(0).toInt - 1).toChar)
    val invalidId = ""

    "existing id test be valid" in {
      status(controller.getProduct(existingId).apply(FakeRequest())) mustBe 200
    }

    "none existing id return HTTP 404" in{
      status(controller.getProduct(noneId).apply(FakeRequest())) mustBe 404
    }

    "invalid id test return HTTP 404" in {
      status(controller.getProduct(invalidId).apply(FakeRequest())) mustBe 404
    }
  }

  implicit lazy val materializer: Materializer = app.materializer
  "POST <root>/rest/bakery/product test" should {
    "be valid" in {
      val environment = Environment.simple()
      val controller = new ProductController(Helpers.stubControllerComponents(), environment)
      val requestJson = Json.obj(
        "name" -> "Kunafa",
        "quantity" -> 2,
        "price" -> 12.5
      )

      val request = FakeRequest(POST, "rest/bakery/product").withHeaders(CONTENT_TYPE -> "application/json")
        .withBody(requestJson)

      val result = call(controller.addProduct(), request)
      status(result) mustBe 200
    }
  }

  "PUT <root>/rest/bakery/product/:id" should {
    val environment = Environment.simple()
    val controller = new ProductController(Helpers.stubControllerComponents(), environment)

    val result: Future[Result] = controller.getInventory().apply(FakeRequest())
    val json: JsValue = Json.parse(contentAsString(result))
    val element = json.as[Seq[JsObject]].head

    val existingId = (element \ "id").as[String]
    val noneId = existingId.replace(existingId.charAt(0), (existingId.charAt(0).toInt - 1).toChar)
    val invalidId = ""

    val requestJson = Json.obj(
      "name" -> (element \ "name").as[String],
      "quantity" -> ((element \ "quantity").as[Int] + 1),
      "price" -> ((element \ "price").as[Double] + 1)
    )

    val requestGood = FakeRequest(PUT, "rest/bakery/product/"+existingId).withHeaders(CONTENT_TYPE -> "application/json")
      .withBody(requestJson)

    val requestNoId = FakeRequest(PUT, "rest/bakery/product/" + noneId).withHeaders(CONTENT_TYPE -> "application/json")
      .withBody(requestJson)

    val requestInvalid = FakeRequest(PUT, "rest/bakery/product/" + invalidId).withHeaders(CONTENT_TYPE -> "application/json")
      .withBody(requestJson)

    val resultGood = call(controller.updateProduct(existingId), requestGood)
    val resultNoId = call(controller.updateProduct(noneId), requestNoId)
    val resultInvalid = call(controller.updateProduct(invalidId), requestInvalid)

    "existing id test be valid" in {
      status(resultGood) mustBe 200
    }

    "none existing id test return HTTP 404" in {
      status(resultNoId) mustBe 404
    }

    "Invalid id test return HTTP 404" in {
      status(resultInvalid) mustBe 404
    }
  }

  "DELETE <root>/rest/bakery/product/:id test" should {
    val environment = Environment.simple()
    val controller = new ProductController(Helpers.stubControllerComponents(), environment)

    val result: Future[Result] = controller.getInventory().apply(FakeRequest())
    val json: JsValue = Json.parse(contentAsString(result))
    val element = json.as[Seq[JsObject]].head

    val existingId = (element \ "id").as[String]
    val noneId = existingId.replace(existingId.charAt(0), (existingId.charAt(0).toInt - 1).toChar)
    val invalidId = ""

    "existing id test be valid" in {
      status(controller.deleteProduct(existingId).apply(FakeRequest())) mustBe 200
    }

    "none existing id test return HTTP 404" in {
      status(controller.deleteProduct(noneId).apply(FakeRequest())) mustBe 404
    }

    "Invalid id test return HTTP 404" in {
      status(controller.deleteProduct(invalidId).apply(FakeRequest())) mustBe 404
    }
  }
}
