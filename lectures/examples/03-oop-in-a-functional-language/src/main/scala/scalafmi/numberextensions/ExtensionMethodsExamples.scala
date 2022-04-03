package scalafmi.numberextensions

import mathematical.Rational

@main def extensionMethodsExamples =
  // Extensions methods are automatically available
  // because they are defined in Rational's companion object
  val xs: List[Rational] = List(Rational(1, 2), Rational(3, 4))
  xs.total
  xs.avg


  // Extension methods that become available when imported:
  import scalafmi.numberextensions.*

  42 ** 3
  math.Pi.squared


  // Examples from the standard library:
  import scala.concurrent.duration.DurationInt

  Map(1 -> "One", 2 -> "Two")
  "abcdef".take(2)
  5.seconds
