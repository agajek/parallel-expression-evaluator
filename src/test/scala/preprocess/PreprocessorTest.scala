package preprocess

import org.scalatest.{FreeSpec, Matchers}
import preprocess.Preprocessor.ValidationError

import scala.util.{Failure, Success}

class PreprocessorTest extends FreeSpec with Matchers {

  "should tokenize expression" in {
    //given
    val expression = "2*(1-1)+3*(1-3+4)+10/2"

    //when
    val tokenized = Preprocessor.tokenize(expression)

    //then
    tokenized.mkString shouldEqual expression
  }

  "should pass validation if paretheses are balanced" in {
    //when
    val a = "2*(1-1)+3*(1-3+4)+10/2"
    val b = "2*(1-1)+3*(1-(3+4)+10/2)"

    //then
    Preprocessor.validateParenthesess(a) shouldEqual Success(a)
    Preprocessor.validateParenthesess(b) shouldEqual Success(b)
  }

  "should not pass validation if paretheses are not balanced" in {
    //when
    val a = "2*(1-1)+3*(1-3+4)+10/2)"
    val b = "2*(1-1)+(3*(1-(3+4)+10/2)"

    //then
    Preprocessor.validateParenthesess(a) shouldEqual Failure(ValidationError("Parentheses are not balanced"))
    Preprocessor.validateParenthesess(b) shouldEqual Failure(ValidationError("Parentheses are not balanced"))
  }

  "should pass validation when expression has proper format" in {
    val a = "2*(1-1)+3*(1-3+4)+10/2)"

    Preprocessor.validateExpressionFormat(a) shouldEqual Success(a)
  }

  "should not pass validation when expression has no proper format" in {
    val a = "2+*3*10"

    intercept[ValidationError] {
      Preprocessor.validateExpressionFormat(a).get
    }
  }

  "should not pass validation when expression contains not allowed characters" in {
    val a = "2+2^3"

    intercept[ValidationError] {
      Preprocessor.validateExpressionFormat(a).get
    }
  }
}
