package sequential

object Expression {

  type Stacks = (List[Operator], List[String])

  def convertToPostfix(expression: List[String]): List[String] = {
    val wrappedWithParentheses = "(" :: expression ::: List(")")
    val (operators, stack) = processExpression(wrappedWithParentheses)
    stack.head.split("\\s").toList
  }

  private def processExpression(expression: List[String]): Stacks =
    expression.foldLeft((List.empty[Operator], List.empty[String])) { case ((ops, stack), s) =>
      if(s.isOperator)
        processOperator(s)(ops, stack)
      else
        (ops, s :: stack)
    }

  private def processOperator(s: String)(ops: List[Operator], stack: List[String]): Stacks = {
    val precedence = Map( "(" -> 0, ")" -> 0, "+" -> 1, "-" -> 1, "*" -> 2, "/" -> 2)

    if (ops.isEmpty || s == "(" || (precedence(s) > precedence(ops.head.op)))
      (Operator(s) :: ops, stack)
    else if(ops.head.shouldBeSkipped)
      (ops.tail, stack)
    else
      processOperator(s)(ops.tail, pushEvaluated(stack, ops.head))
  }

  private def pushEvaluated(vals: List[String], operator: Operator): List[String] = {
    val b :: a :: tail = vals
    s"$a $b ${operator.op}" :: tail
  }

  case class Operator(op: String) extends AnyVal {
    def shouldBeSkipped: Boolean = op == "("
  }

  private implicit class enrichOperators(val sa: String) {
    val operators = Set("(", ")", "+", "-", "*", "/")

    def isOperator: Boolean = operators.contains(sa)
  }
}

