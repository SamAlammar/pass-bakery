import controllers.ProductController

import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.Environment
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import java.time.LocalDateTime

/** A test class for ProductController to see whether it returns the correct signal
 *
 */
class ProductControllerTest extends PlaySpec with Results {

  "Example Page#index" should {
    "should be valid" in {
      val environment = Environment.simple()

      val controller             = new ProductController(Helpers.stubControllerComponents(), environment)
      val result: Future[Result] = controller.getStatus().apply(FakeRequest())

      result.value.get.get.toString() mustBe "Result(200, TreeMap())"
    }
  }
}
