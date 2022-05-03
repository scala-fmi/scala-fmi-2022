package math

import scala.annotation.tailrec

object ListOrderingDemo extends App:
  given [A : Ordering]: Ordering[List[A]] = (x: List[A], y: List[A]) =>
    val aOrdering = Ordering[A]
    import aOrdering.mkOrderingOps

    @tailrec
    def loop(x: List[A], y: List[A]): Int =
      if x.isEmpty && y.isEmpty then 0
      else if x.isEmpty then -1
      else if y.isEmpty then 1
      else if x.head < y.head then -1
      else if y.head < x.head then 1
      else loop(x.tail, y.tail)

    loop(x, y)

  val sortedList = List(
    List(1, 2, 3),
    List(1, 3, 2),
    List(3, 4),
    List.empty[Int],
    List(1, 1)
  ).sorted

  println(sortedList)
