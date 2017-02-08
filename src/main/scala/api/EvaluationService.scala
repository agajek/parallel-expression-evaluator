package api

import actors.NodeActor
import actors.NodeActor.{Get, Value}
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import cats.data.ValidatedNel
import cats.implicits._
import preprocess.Preprocessor._
import sequential.Expression.convertToPostfix
import sequential.ExpressionTree.buildTree

import scala.concurrent.Future
import scala.concurrent.duration._

trait EvaluationService {

  def evaluateExpression(expression: String): ValidatedNel[String, Future[Double]]
}

class ActorEvaluationService(implicit actorSystem: ActorSystem) extends EvaluationService {

  implicit val timeout = Timeout(3 seconds)
  implicit val ctx = actorSystem.dispatcher

  override def evaluateExpression(expression: String): ValidatedNel[String, Future[Double]] = {

    val validation = validateParenthesess(expression) |@| validateExpressionFormat(expression)
    validation.map { (_, validated) =>
      val tokenized = tokenize(validated)
      val postfix = convertToPostfix(tokenized)
      val tree = buildTree(postfix)
      val root = actorSystem.actorOf(NodeActor.props(tree))
      (root ? Get).mapTo[Value].map(_.value)
    }

  }

}
