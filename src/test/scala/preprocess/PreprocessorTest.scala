package preprocess

import org.scalatest.{FreeSpec, Matchers}
import preprocess.Preprocessor.ValidationError

import scala.util.{Failure, Success}

class PreprocessorTest extends FreeSpec with Matchers {

  "should tokenize expression" in {
    //given
    val expression = "2*(1-1)+3*(1-3+4)+10/2"

    //when
    val tokenized = Preprocessor.tokenize(expression.toList)

    //then
    tokenized.mkString shouldEqual expression
  }

  "should pass validation if paretheses are balanced" in {
    //when
    val a = Preprocessor.tokenize("2*(1-1)+3*(1-3+4)+10/2".toList)
    val b = Preprocessor.tokenize("2*(1-1)+3*(1-(3+4)+10/2)".toList)

    //then
    Preprocessor.validateParenthesess(a) shouldEqual Success(a)
    Preprocessor.validateParenthesess(b) shouldEqual Success(b)
  }

  "should not pass validation if paretheses are not balanced" in {
    //when
    val a = Preprocessor.tokenize("2*(1-1)+3*(1-3+4)+10/2)".toList)
    val b = Preprocessor.tokenize("2*(1-1)+(3*(1-(3+4)+10/2)".toList)

    //then
    Preprocessor.validateParenthesess(a) shouldEqual Failure(ValidationError("Parentheses are not balanced"))
    Preprocessor.validateParenthesess(b) shouldEqual Failure(ValidationError("Parentheses are not balanced"))

  }
}
