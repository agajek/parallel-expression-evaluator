package preprocess

import scala.util.{Failure, Success, Try}

object Preprocessor {
  def tokenize(expression: String): List[String] = {
    def f(list: List[Char], number: String = ""): List[String] = list match {
      case Nil if number.nonEmpty => number :: Nil
      case Nil => Nil
      case x :: xs if x.isDigit => f(xs, number.trim + x)
      case x :: xs if number.nonEmpty => number :: x.toString :: f(xs)
      case x :: xs => x.toString :: f(xs)
    }

    f(expression.toList)
  }

  def validateParenthesess(expression: String): Try[String] = {
    def f(list: List[Char], stack: List[String]): List[String] = list match {
      case Nil => stack
      case '(' :: xs => f(xs, "(" :: stack)
      case ')' :: xs if stack.nonEmpty => f(xs, stack.tail)
      case ')' :: xs => f(xs, ")" :: stack)
      case x :: xs => f(xs, stack)
    }

    if (f(expression.toList, Nil).isEmpty)
      Success(expression)
    else
      Failure(ValidationError("Parentheses are not balanced"))
  }

  def validateExpressionFormat(expression: String): Try[String] = {
    val regex = "^\\d+(?:[+*-/]\\d+)+$"
    val escapedExpression = expression.replaceAll("[() ]", "")

    if(escapedExpression.matches(regex))
      Success(expression)
    else
      Failure(ValidationError("Provided expression has not valid form or contains not allowed characters"))
  }

  case class ValidationError(msg: String) extends Exception(msg)

}


