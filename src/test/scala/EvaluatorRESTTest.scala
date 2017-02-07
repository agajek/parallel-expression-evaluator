import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import api.EvaluatorREST.{EvaluationRequest, EvaluationResponse}
import api.{ActorEvaluationService, EvaluatorREST}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}
import org.scalatest.{FreeSpec, Matchers}
import preprocess.Preprocessor

class EvaluatorRESTTest extends FreeSpec with Matchers with ScalatestRouteTest with Json4sSupport {

  implicit val formats = DefaultFormats
  implicit val serialization = jackson.Serialization

  "REST API " - {
    val service = new ActorEvaluationService()
    val rest = new EvaluatorREST(service)
    "should evaluate valid input expression with parentheses" in {
      Post("/evaluate", EvaluationRequest("(1+3)*(4+5)")) ~> rest.route ~> check {
        status == StatusCodes.OK
        responseAs[EvaluationResponse] shouldEqual EvaluationResponse(36.0)
      }
    }

    "should evaluate valid input expression with proper operator's precedence" in {
      Post("/evaluate", EvaluationRequest("2+2*2")) ~> rest.route ~> check {
        status == StatusCodes.OK
        responseAs[EvaluationResponse] shouldEqual EvaluationResponse(6)
      }
    }

    "should response with validation error when expression format is not met" in {
      Post("/evaluate", EvaluationRequest("2+2**2")) ~> rest.route ~> check {
        status == StatusCodes.BadRequest
        responseAs[List[String]] shouldEqual List(Preprocessor.expressionFormatValidationError)
      }
    }

    "should response with accumulated validation errors when expression format is not met and parentheses not balanced" in {
      Post("/evaluate", EvaluationRequest("2+2**)2")) ~> rest.route ~> check {
        status == StatusCodes.BadRequest
        responseAs[List[String]] should contain allOf (Preprocessor.expressionFormatValidationError, Preprocessor.parenthesesValidationError)
      }
    }
  }
}

