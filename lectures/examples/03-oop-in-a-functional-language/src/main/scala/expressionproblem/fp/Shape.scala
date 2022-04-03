package expressionproblem.fp

trait Shape
case class Circle(r: Double) extends Shape
case class Rectangle(a: Double, b: Double) extends Shape
case class Square(a: Double) extends Shape

def area(s: Shape): Double = s match
  case Circle(r) => math.Pi * r * r
  case Rectangle(a, b) => a * b
  case Square(a) => a * a


def circumference(s: Shape): Double = s match
  case Circle(r) => 2 * math.Pi * r
  case Rectangle(a, b) => 2 * (a + b)
  case Square(a) => 4 * a
