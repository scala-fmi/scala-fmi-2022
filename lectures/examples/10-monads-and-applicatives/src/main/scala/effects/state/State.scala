package effects.state

import effects.Monad

case class State[S, A](run: S => (S, A))

object State:
  // [A] =>> State[S, A] is a type lambda – a type-level function which
  // accepts a type parameter and produces a specific type – in this case
  // the function accepts the parameter A and produces the type State[S, A].
  //
  // In meaning this is equivalent to having:
  //
  // type S = ???
  // type StateMonad[A] = State[S, A]
  //
  // and to passing StateMonad to `Monad` as type parameter. StateMonad is a type-level function too,
  // but unlike the lambda above it has a name
  //
  // Both of these lines are equivalent in Scala 3:
  // type StateMonad[A] = State[S, A]
  // type StateMonad = [A] =>> State[S, A]

  given [S]: Monad[[A] =>> State[S, A]] with
    extension [A](fa: State[S, A])
      def flatMap[B](f: A => State[S, B]): State[S, B] = State { s1 =>
        val (s2, a) = fa.run(s1)
        f(a).run(s2)
      }

    def unit[A](a: A): State[S, A] = State(s => (s, a))
