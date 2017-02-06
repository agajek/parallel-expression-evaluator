package sequential

sealed trait ExpressionTree
case class Leaf(value: String) extends ExpressionTree
case class Node(operator: String, left: ExpressionTree, right: ExpressionTree) extends ExpressionTree

object ExpressionTree {

  def buildTree(tokens: List[String], stack: List[ExpressionTree] = Nil): ExpressionTree = tokens match {
    case Nil =>
      stack.head

    case x :: xs if x.forall(_.isDigit) =>
      buildTree(xs, Leaf(x) :: stack)

    case x :: xs =>
      val b :: a :: tail = stack
      buildTree(xs, Node(x, a, b) :: tail)
  }
}

