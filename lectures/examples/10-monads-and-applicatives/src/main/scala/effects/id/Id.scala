package effects.id

import effects.Monad

object Id:
  type Id[A] = A

  given Monad[Id] with
    def unit[A](a: A): Id[A] = a

    extension [A](fa: Id[A])
      def flatMap[B](f: A => Id[B]): Id[B] = f(fa)

@main def runIdDemo =
  import Id.Id
  import Id.given Monad[Id]

  val r = for
    a <- "Hello, "
    b <- "World!"
  yield a + b

  val a = "Hello, "
  val b = "World!"
  val r2 = a + b

  Monad[Id].map2(a, b)(_ + _)
  a + b

  println(r)
