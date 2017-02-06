import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import akka.util.Timeout
import api.{ActorEvaluationService, EvaluationService, EvaluatorREST}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem("ExpressionCalculator")
  implicit val ctx = system.dispatcher
  implicit val timeout = Timeout(5 seconds)

  val service = new ActorEvaluationService

  val rest = new EvaluatorREST(service)

  rest.startServer("localhost", 8080, ServerSettings(ConfigFactory.load))

}
