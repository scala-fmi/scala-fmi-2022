package cats

import cats.syntax.foldable.*

@main def runFoldableDemo =
  def doSomething[F[_] : Foldable, A : Monoid](f: F[A]): A =
    println {
      f.forall(_ != Monoid[A].empty)
    }

    println {
      f.foldMap(_.toString)
    }

    f.combineAll


  println {
    doSomething(List(10, 20, 30, 42))
  }
