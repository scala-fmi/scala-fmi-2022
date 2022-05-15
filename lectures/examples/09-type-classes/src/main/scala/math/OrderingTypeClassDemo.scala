package math

import scala.math.abs

@main def runOrderingTypeClassDemo =
  def quickSort[T](xs: List[T])(using m: Ordering[T]): List[T] =
    import m.mkOrderingOps

    xs match
      case Nil => Nil
      case x :: rest =>
        val (before, after) = rest partition { _ < x }
        quickSort(before) ++ (x :: quickSort(after))

  given Ordering[Int] = Ordering.Int.reverse

  quickSort(List(5, 1, 2, 3)) // List(5, 3, 2, 1)
  quickSort(List(-5, 1, 2, -2, 3)) // List(3, 2, 1, -2, -5)
  quickSort(List(-5, 1, 2, -2, 3))(using Ordering.by(abs)) // List(-5, 3, 2, -2, 1)
