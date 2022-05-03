package cats

import math.Rational

import cats.instances.all.*
// We are importing only the syntax we are going to use
import cats.syntax.monoid.*

@main def runCatsMonoidDemo =
  (2, 3) |+| (4, 5)

  given rationalMonoid: Monoid[Rational] with
    def empty: Rational = 0
    def combine(x: Rational, y: Rational): Rational = x + y

  val map1 = Map(1 -> (2, Rational(3, 2)), 2 -> (3, Rational(4)))
  val map2 = Map(2 -> (5, Rational(6)), 3 -> (7, Rational(8, 3)))

  println(map1 |+| map2)
