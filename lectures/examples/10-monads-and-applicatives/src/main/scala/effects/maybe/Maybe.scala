package effects.maybe

import effects.Monad

sealed trait Maybe[+A]
case class Just[+A](a: A) extends Maybe[A]
case object Nthng extends Maybe[Nothing]

object Maybe:
  given Monad[Maybe] with
    def unit[A](a: A): Maybe[A] = Just(a)

    extension [A](fa: Maybe[A])
      def flatMap[B](f: A => Maybe[B]): Maybe[B] = fa match
        case Just(a) => f(a)
        case _ => Nthng

@main def runMaybeDemo =
  def f(n: Int): Maybe[Int] = Just(n + 1)
  def g(n: Int): Maybe[Int] = Just(n * 2)
  def h(n: Int): Maybe[Int] = Just(n * n)

  val a = 3

  val result = for
    b <- f(a)
    c <- g(b * 2)
    d <- h(b + c)
  yield a * b * d
  println(result)

  val maybe1: Maybe[Int] = Just(42)
  val maybe2: Maybe[Int] = Just(10)

  Monad[Maybe].map2(maybe1, maybe2)(_ + _)

  val listOfMaybes = List(Just(1), Nthng, Just(2))
