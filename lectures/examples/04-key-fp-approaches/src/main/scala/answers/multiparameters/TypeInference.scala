package answers.multiparameters

import scala.annotation.tailrec

object TypeInference:
  def mapSL[A, B](la: List[A], f: A => B): List[B] =
    @tailrec
    def loop(rest: List[A], acc: List[B] = Nil): List[B] =
      if rest.isEmpty then acc
      else loop(rest.tail, f(rest.head) :: acc)

    loop(la).reverse

  def mapML[A, B](la: List[A])(f: A => B): List[B] =
    @tailrec
    def loop(rest: List[A], acc: List[B] = Nil): List[B] =
      if rest.isEmpty then acc
      else loop(rest.tail, f(rest.head) :: acc)

    loop(la).reverse

  mapSL(List(1, 2, 3), _ * 2) // Does not compile on Scala 2, but does on Scala 3
  mapSL(List(1, 2, 3), (_: Int) * 2)

  mapML(List(1, 2, 3))(_ * 2) // Type inference works in both Scala 2 and Scala 3
  mapML(List(1, 2, 3))((_: Int) * 2)
