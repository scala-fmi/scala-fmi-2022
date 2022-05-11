package answers

import effects.Functor

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

trait Monad[F[_]] extends Functor[F]:
  def unit[A](a: A): F[A]
  extension [A](fa: F[A])
    def flatMap[B](f: A => F[B]): F[B]

    def map[B](f: A => B): F[B] = fa.flatMap(a => unit(f(a)))

  def compose[A, B, C](f: A => F[B], g: B => F[C]): A => F[C] = a => f(a).flatMap(g)

  extension [A](ffa: F[F[A]])
    def flatten: F[A] = ffa.flatMap(x => x)

  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] = fa.flatMap(a => map(fb)(b => (a, b)))
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = zip(fa, fb).map(f.tupled)

  extension [A](lfa: List[F[A]])
    def sequence: F[List[A]] =
      lfa.foldRight(unit(List[A]())) { (next, acc) =>
        map2(next, acc)(_ :: _)
      }

    def sequence2: F[List[A]] =
      traverse(lfa)(fa => fa)

  extension [A](as: List[A])
    def traverse[B](f: A => F[B]): F[List[B]] =
      as.foldRight(unit(List[B]()))((a, mbs) => map2(f(a), mbs)(_ :: _))

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
