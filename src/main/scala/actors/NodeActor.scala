package actors

import actors.NodeActor.{Get, Value, unexpectedMessageLog}
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Stash}
import sequential.{ExpressionTree, Leaf, Node}

class NodeActor(tree: ExpressionTree) extends Actor with Stash with ActorLogging {

  tree match {
    case Node(operator, left, right) =>
      val leftRef = context.actorOf(NodeActor.props(left))
      val rightRef = context.actorOf(NodeActor.props(right))

      context.become(twoChildren(operator, leftRef, rightRef))

    case Leaf(value) =>
      context.parent ! Value(value.toDouble)
  }

  override def receive: Receive = {
    case msg => log.warning(unexpectedMessageLog(msg.toString, "receive"))
  }

  def twoChildren(op: String, left: ActorRef, right: ActorRef): Receive = {
    case Value(leftVal) if sender() == left =>
      context.become(leftChildAcquired(op, leftVal))

    case Value(rightVal) if sender() == right =>
      context.become(rightChildAcquired(op, rightVal))

    case Get => stash()
    case msg => log.warning(unexpectedMessageLog(msg.toString, "twoChildren"))
  }

  def leftChildAcquired(op: String, leftVal: Double): Receive = {
    case Value(rightVal) => evaluate(op, leftVal, rightVal)

    case Get => stash()
    case msg => log.warning(unexpectedMessageLog(msg.toString, "leftChildAcquired"))
  }

  def rightChildAcquired(op: String, rightVal: Double): Receive = {
    case Value(leftVal) => evaluate(op, leftVal, rightVal)

    case Get => stash()
    case msg => log.warning(unexpectedMessageLog(msg.toString, "rightChildAcquired"))
  }

  def evaluated(value: Double): Receive = {
    case Get => sender() ! Value(value)
  }

  private def evaluate(op: String, leftVal: Double, rightVal: Double) = {
    def eval(operator: String): (Double, Double) => Double = operator match {
      case "+" => (a: Double, b: Double) => a + b
      case "-" => (a: Double, b: Double) => a - b
      case "/" => (a: Double, b: Double) => a / b
      case "*" => (a: Double, b: Double) => a * b
    }

    val evaluatedValue = eval(op)(leftVal, rightVal)

    unstashAll()
    context.parent ! Value(evaluatedValue)
    context.become(evaluated(evaluatedValue))
  }

}

object NodeActor {

  def props(tree: ExpressionTree): Props = Props(new NodeActor(tree))

  def unexpectedMessageLog(msg: String, state: String): String =
    s"Received unexpected message $msg in state $state"

  case class Value(value: Double)

  case object Get
}
