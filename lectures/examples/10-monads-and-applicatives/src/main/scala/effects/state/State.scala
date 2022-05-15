package effects.state

import effects.Monad

case class State[S, A](run: S => (S, A))

object State:
  // [A] =>> State[S, A] is a type lambda – a type-level function which
  // accepts a type parameter and product a specific type – in this case
  // the function accepts the parameter A and produces the type State[S, A].
  //
  // In meaning this is equivalent to having
  // type S = ???
  // type StateMonad[A] = State[S, A]
  // In the above code StateMonad is also a type-level function, but unlike the lambda
  // above it has a name

  given [S]: Monad[[A] =>> State[S, A]] with
    extension [A](fa: State[S, A])
      def flatMap[B](f: A => State[S, B]): State[S, B] = State { s1 =>
        val (s2, a) = fa.run(s1)
        f(a).run(s2)
      }

    def unit[A](a: A): State[S, A] = State(s => (s, a))
