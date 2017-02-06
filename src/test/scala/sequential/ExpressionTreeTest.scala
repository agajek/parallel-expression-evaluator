package sequential

import org.scalatest.{FreeSpec, Matchers}

class ExpressionTreeTest extends FreeSpec with Matchers {

  "should generate tree representation of expression given in postfix notation" in {
    val postfitx = List("2", "1", "1", "-", "*", "3", "1", "3", "-", "4", "+", "*", "+", "10", "2", "/", "+")
    val expectedTree = Node("+",Node("+",Node("*",Leaf("2"),Node("-",Leaf("1"),Leaf("1"))),
      Node("*",Leaf("3"),Node("+",Node("-",Leaf("1"),Leaf("3")),Leaf("4")))),Node("/",Leaf("10"),Leaf("2")))

    ExpressionTree.buildTree(postfitx) shouldEqual expectedTree
  }

}
