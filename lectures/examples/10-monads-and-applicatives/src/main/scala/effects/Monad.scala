package effects

import effects.Functor

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

// We can make our Monad extend Applicative as well
// This way it will get all the useful methods from Applicative
trait Monad[F[_]] extends Applicative[F]:
  // Base operations:
  def unit[A](a: A): F[A]
  extension [A](fa: F[A])
    def flatMap[B](f: A => F[B]): F[B]

  // Implementation for Applicative base map2 base operation:
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = fa.flatMap(a => fb.map(b => f(a, b)))

  // we can override an operation from Applicative if we decide to:
  extension [A](fa: F[A])
    override def map[B](f: A => B): F[B] = fa.flatMap(a => unit(f(a)))

  // Derived operations:
  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = a => f(a).flatMap(g)

  extension [A](ffa: F[F[A]])
    def flatten: F[A] = ffa.flatMap(x => x)

  // map3, map4, apply, sequence, traverse, etc. come from applicative

  // As an example here we will override zip as well
  override def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    // Having flatMap and map allows us to use for comprehensions:
    for
      a <- fa
      b <- fb
    yield (a, b)

    // The above de-sugars to:
    // fa.flatMap(a => fb.map(b => (a, b)))

object Monad:
  def apply[F[_]](using m: Monad[F]): Monad[F] = m

  // Implementing monads from the Scala library
  given Monad[Option] with
    def unit[A](a: A): Option[A] = Some(a)

    extension [A](m: Option[A])
      def flatMap[B](f: A => Option[B]): Option[B] = m.flatMap(f)

      // we can also override some methods with more performant implementation
      override def map[B](f: A => B): Option[B] = m.map(f)

  given Monad[Try] with
    def unit[A](a: A): Try[A] = Success(a)

    extension [A](m: Try[A])
      def flatMap[B](f: A => Try[B]): Try[B] = m flatMap f

  given Monad[List] with
    def unit[A](a: A): List[A] = List(a)

    extension [A](m: List[A])
      def flatMap[B](f: A => List[B]): List[B] = m flatMap f

  // givens can accept context parameters
  given (using ec: ExecutionContext): Monad[Future] with
    def unit[A](a: A): Future[A] = Future(a) // Note that in our definition `a` is not passed by name and thus
                                             // its value won't be evaluated asynchronously
                                             // We will fix this later with more suitable type class

    extension [A](m: Future[A])
      def flatMap[B](f: A => Future[B]): Future[B] = m flatMap f

  given [E]: Monad[[A] =>> Either[E, A]] with
    def unit[A](a: A): Either[E, A] = Right(a)

    extension [A](fa: Either[E, A])
      def flatMap[B](f: A => Either[E, B]): Either[E, B] = fa.flatMap(f)
