package sequential

import org.scalatest.{FreeSpec, Matchers}

class ExpressionTest extends FreeSpec with Matchers {

  "convert to postfix form" in {
    val a = "2 * ( 1 - 1 ) + 3 * ( 1 - 3 + 4 ) + 10 / 2".split(" ").toList
    Expression.convertToPostfix(a) shouldEqual List("2", "1", "1", "-", "*", "3", "1", "3", "-", "4", "+", "*", "+", "10", "2", "/", "+")

    val b = "( 1 - 1 ) * 2 + 3 * ( 1 - 3 + 4 ) + 10 / 2".split(" ").toList
    Expression.convertToPostfix(b) shouldEqual List("1", "1", "-", "2", "*", "3", "1", "3", "-", "4", "+", "*", "+", "10", "2", "/", "+")

    val e = "10 / 2".split(" ").toList
    Expression.convertToPostfix(e) shouldEqual List("10", "2", "/")
  }

}
