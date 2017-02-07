import actors.NodeActor
import actors.NodeActor.{Get, Value}
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{FreeSpecLike, Matchers}
import preprocess.Preprocessor
import sequential.{Expression, ExpressionTree}

class ExpressionEvaluatorTest extends TestKit(ActorSystem("EvaluatorTest")) with FreeSpecLike with Matchers with ImplicitSender {


  "should evaluate input expression" in {
    val expression = Preprocessor.tokenize("(2*(1-1)+3*(1-3+4)+10/2)")
    val postfix = Expression.convertToPostfix(expression)
    val tree = ExpressionTree.buildTree(postfix)

    val root = system.actorOf(NodeActor.props(tree))

    root ! Get
    expectMsg(Value(11))
  }

}

