package effects

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

trait Monad[F[_]]:
  // Exercise 1: Implement compose using flatMap and vice versa

  def unit[A](a: A): F[A]
  extension [A](fa: F[A])
    def flatMap[B](f: A => F[B]): F[B]

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = a => f(a).flatMap(g)

















  // Exercise 2: Implement the functions below using the primitives
  extension [A](fa: F[A])
    def map[B](f: A => B): F[B] = fa.flatMap(a => unit(f(a)))

  extension [A](ffa: F[F[A]])
    def flatten: F[A] = ffa.flatMap(x => x)

  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] = fa.flatMap(a => map(fb)(b => (a, b)))
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = zip(fa, fb).map(f.tupled)

object Monad:
  def apply[F[_]](using m: Monad[F]): Monad[F] = m

  // Implementing monads from the Scala library
  given Monad[Option] with
    def unit[A](a: A): Option[A] = Some(a)

    extension [A](m: Option[A])
      def flatMap[B](f: A => Option[B]): Option[B] = m flatMap f

      // we can also override some methods with more performant implemenation
      override def map[B](f: A => B): Option[B] = m map f

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
    def unit[A](a: A): Future[A] = Future(a)

    extension [A](m: Future[A])
      def flatMap[B](f: A => Future[B]): Future[B] = m flatMap f

  given [E]: Monad[[A] =>> Either[E, A]] with
    def unit[A](a: A): Either[E, A] = Right(a)

    extension [A](fa: Either[E, A])
      def flatMap[B](f: A => Either[E, B]): Either[E, B] = fa.flatMap(f)
