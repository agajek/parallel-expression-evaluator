package api

import scala.concurrent.Future

trait EvaluationService {

  def evaluateExpression(expression: String): Future[Double]
}