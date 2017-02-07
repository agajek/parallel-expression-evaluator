package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{HttpApp, Route}
import api.EvaluatorREST.{EvaluationRequest, EvaluationResponse}
import cats.data.Validated.{Invalid, Valid}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}

class EvaluatorREST(service: EvaluationService) extends HttpApp with Json4sSupport {

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats

  def route: Route =
    path("evaluate") {
      post { entity(as[EvaluationRequest]) { request: EvaluationRequest =>
        service.evaluateExpression(request.expression) match {
          case Valid(result) =>
            onSuccess(result) { value => complete(EvaluationResponse(value)) }
          case Invalid(message) =>
            complete(400 -> message.toList)
        }
      }
    }
  }
}


object EvaluatorREST {
  case class EvaluationRequest(expression: String)
  case class EvaluationResponse(result: Double)
}
