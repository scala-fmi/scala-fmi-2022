package cats

import math.Rational
import cats.instances.all.*
import cats.syntax.eq.*

case class Cat(name: String)
case class Dog(name: String)

@main def runEqDemo =
  // compiles
  Some(Rational(2)) == Rational(2)
  Cat("Vivian") == Dog("Rony")

  // won't compile, uses the Eq typeclass
  //   Some(Rational(2)) === Rational(2)
  //   Cat("Vivian") === Dog("Rony")
  // won't compile either
  //  "0" === 2

  // compiles
  0 === 2

  given Eq[Rational] with
    def eqv(x: Rational, y: Rational): Boolean = x == y

  println(Rational(5) === Rational(10, 2))
  println(Rational(5, 2) =!= Rational(10, 2))
  // doesn't compile
  // println(Rational(5, 2) === "")
  println(Rational(5, 2) === 2)

  case class Box[+A](a: A):
    def contains[B >: A : Eq](b: B): Boolean = b === a

  Box(1).contains(1)
  // doesn't compile
  // Box(1).contains("")

  // compiles as it doesn't use the type class
  List(1).contains("")
