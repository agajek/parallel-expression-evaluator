package preprocess

import cats.data.NonEmptyList
import cats.data.Validated.{Invalid, Valid}
import org.scalatest.{FreeSpec, Matchers}
import preprocess.Preprocessor._
class PreprocessorTest extends FreeSpec with Matchers {

  "should tokenize expression" in {
    //given
    val expression = "2*(1-1)+3*(1-3+4)+10/2"

    //when
    val tokenized = Preprocessor.tokenize(expression)

    //then
    tokenized.mkString shouldEqual expression
  }

  "should tokenize expression and escape whitespaces" in {
    //given
    val expression = "2*(1-1)+3*  (1-3+4)+10/2"

    //when
    val tokenized = Preprocessor.tokenize(expression)

    //then
    tokenized.mkString shouldEqual expression.replaceAll("\\s+","")
  }

  "should pass validation if paretheses are balanced" in {
    //when
    val a = "2*(1-1)+3*(1-3+4)+10/2"
    val b = "2*(1-1)+3*(1-(3+4)+10/2)"

    //then
    validateParenthesess(a) shouldEqual Valid(a)
    validateParenthesess(b) shouldEqual Valid(b)
  }

  "should not pass validation if paretheses are not balanced" in {
    //when
    val a = "2*(1-1)+3*(1-3+4)+10/2)"
    val b = "2*(1-1)+(3*(1-(3+4)+10/2)"

    //then
    validateParenthesess(a) shouldEqual Invalid(NonEmptyList.of(parenthesesValidationError))
    validateParenthesess(b) shouldEqual Invalid(NonEmptyList.of(parenthesesValidationError))
  }

  "should pass validation when expression has proper format" in {
    val a = "2*(1-1)+3*(1-3+4)+10/2)"

    Preprocessor.validateExpressionFormat(a) shouldEqual Valid(a)
  }

  "should not pass validation when expression has no proper format" in {
    val a = "2+*3*10"

    Preprocessor.validateExpressionFormat(a) shouldEqual Invalid(NonEmptyList.of(expressionFormatValidationError))
  }

  "should not pass validation when expression contains not allowed characters" in {
    val a = "2+2^3"

    Preprocessor.validateExpressionFormat(a) shouldEqual Invalid(NonEmptyList.of(expressionFormatValidationError))
  }
}
