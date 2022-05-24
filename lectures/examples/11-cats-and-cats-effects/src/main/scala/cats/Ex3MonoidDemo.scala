package cats

import cats.syntax.monoid.*
import cats.instances.all.*

@main def runMonoidDemo =
  1 |+| 2
  "ab".combineN(3)

  0.isEmpty

  Semigroup[Int].combineAllOption(List(1, 2, 3))
  Monoid[Int].combineAll(List(1, 2, 3))
