package expressionproblem.oop

trait Shape:
  def area: Double
  def circumference: Double

case class Circle(r: Double) extends Shape:
  def area: Double = math.Pi * r * r
  def circumference: Double = 2 * math.Pi * r

case class Rectangle(a: Double, b: Double) extends Shape:
  def area: Double = a * b
  def circumference: Double = 2 * (a + b)

case class Square(a: Double) extends Shape:
  def area: Double = a * a
  def circumference: Double = 4 * a
