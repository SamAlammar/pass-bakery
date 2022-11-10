import controllers.ProductController

import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.Environment
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

/** A test class for ProductController to see whether it returns the correct signal
 *
 */
class ProductControllerTest extends PlaySpec with Results {

  "getStatus() test" should {
    "be valid" in {
      val environment = Environment.simple()

      val controller             = new ProductController(Helpers.stubControllerComponents(), environment)
      val result: Future[Result] = controller.getStatus().apply(FakeRequest())

      status(result) mustBe 200
    }
  }

  "getInventory() test" should {
    "be valid" in {
      val environment = Environment.simple()

      val controller = new ProductController(Helpers.stubControllerComponents(), environment)
      val result: Future[Result] = controller.getInventory().apply(FakeRequest())

      status(result) mustBe 200
    }
  }
}
