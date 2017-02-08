import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import api.{ActorEvaluationService, EvaluatorREST}
import com.typesafe.config.ConfigFactory

object Main extends App {

  implicit val system = ActorSystem("ExpressionCalculator")
  val config = ConfigFactory.load()
  val service = new ActorEvaluationService
  val rest = new EvaluatorREST(service)

  val host = config.getString("api.host")
  val port = config.getInt("api.port")

  rest.startServer(host, port, ServerSettings(ConfigFactory.load))
}
