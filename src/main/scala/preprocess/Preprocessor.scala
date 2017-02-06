package preprocess

import scala.util.{Failure, Success, Try}

object Preprocessor {
  def tokenize(list: List[Char], number: String = ""): List[String] = list match {
    case Nil if number.nonEmpty => number :: Nil
    case Nil => Nil
    case x :: xs if x.isDigit => tokenize(xs, number.trim + x)
    case x :: xs if number.nonEmpty => number :: x.toString :: tokenize(xs)
    case x :: xs => x.toString :: tokenize(xs)
  }

  def validateParenthesess(expression: List[String]): Try[List[String]] = {
    def f(list: List[String], stack: List[String]): List[String] = list match {
      case Nil => stack
      case "(" :: xs => f(xs, "(" :: stack)
      case ")" :: xs if stack.nonEmpty => f(xs, stack.tail)
      case ")" :: xs => f(xs, ")" :: stack)
      case x :: xs => f(xs, stack)
    }

    if (f(expression, Nil).isEmpty)
      Success(expression)
    else
      Failure(ValidationError("Parentheses are not balanced"))
  }

  case class ValidationError(msg: String) extends Exception(msg)

}


