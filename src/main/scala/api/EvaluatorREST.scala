package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{HttpApp, Route}
import api.EvaluatorREST.{EvaluationRequest, EvaluationResponse}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}

import scala.util.{Failure, Success}

class EvaluatorREST(service: EvaluationService) extends HttpApp with Json4sSupport {

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  def route: Route =
    path("evaluate") {
      post { entity(as[EvaluationRequest]) { request: EvaluationRequest =>
        val requestResult = service.evaluateExpression(request.expression)

        onComplete(requestResult) {
          case Success(value) => complete(EvaluationResponse(value))
          case Failure(ex) => complete(401 -> ex)
        }
      }
    }
  }
}


object EvaluatorREST {
  case class EvaluationRequest(expression: String)
  case class EvaluationResponse(result: Double)
}
