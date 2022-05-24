package cats

import cats.instances.all.*
import cats.syntax.functor.*

@main def runFunctorDemo =
  val ex1 = Option(("a", 0)).swapF
  val ex2 = List(("a", 0), ("b", 1), ("c", 2)).swapF.toMap

  def genericDouble[F[_] : Functor](ints: F[Int]): F[Int] = ints.map(_ * 2)

  println(ex1)
  println(ex2)
  println(genericDouble(Option(10)))
  println(genericDouble(List(10, 20, 30)))

  println {
    Option(10).as(42)
  }

//  def doSomething: Future[Unit] = ???
  println {
    Option(10).void
  }

  trait Fruit
  case class Apple(color: String) extends Fruit
  case class Orange(sort: String) extends Fruit

  val setFruit: Option[Fruit] = Option(Apple("red")).widen