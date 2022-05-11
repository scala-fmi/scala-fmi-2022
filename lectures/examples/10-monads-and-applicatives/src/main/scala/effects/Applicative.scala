package effects

import scala.util.Try

trait Applicative[F[_]] extends Functor[F]:

  // primitive combinators
  def map2[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C]
  def unit[A](a: A): F[A]

  // derived combinators
  extension [A](fa: F[A])
    def map[B](f: A => B): F[B] = map2(fa, unit(()))((a, _) => f(a))

  def apply[A, B](fab: F[A => B])(fa: F[A]): F[B] = map2(fab, fa)(_(_))

  def zip[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    map2(fa, fb)((_, _))

  def map3[A, B, C, D](fa: F[A], fb: F[B], fc: F[C])(f: (A, B, C) => D): F[D] =
    val product = zip(zip(fa, fb), fc)
    map(product) { case ((a, b), c) =>
      f(a, b, c)
    }

  /*
  The pattern is simple. We just curry the function
  we want to lift, pass the result to `unit`, and then `apply`
  as many times as there are arguments.
  Each call to `apply` is a partial application of the function
   */
  def map4[A, B, C, D, E](fa: F[A], fb: F[B], fc: F[C], fd: F[D])(f: (A, B, C, D) => E): F[E] =
    apply(apply(apply(apply(unit(f.curried))(fa))(fb))(fc))(fd)

  extension [A](lfa: List[F[A]])
    def sequence: F[List[A]] = lfa.traverse(fa => fa)

  extension [A](as: List[A])
    def traverse[B](f: A => F[B]): F[List[B]] =
      as.foldRight(unit(List[B]()))((a, mbs) => map2(f(a), mbs)(_ :: _))

object Applicative:
  def apply[F[_]](using a: Applicative[F]): Applicative[F] = a

  given Applicative[Option] with
    def map2[A, B, C](fa: Option[A], fb: Option[B])(f: (A, B) => C): Option[C] = (fa, fb) match
      case (Some(a), Some(b)) => Some(f(a, b))
      case (_, _) => None

    def unit[A](a: A): Option[A] = Some(a)

  given [L]: Applicative[[R] =>> Either[L, R]] with
    def map2[A, B, C](fa: Either[L, A], fb: Either[L, B])(f: (A, B) => C): Either[L, C] = (fa, fb) match
      case (Right(a), Right(b)) => Right(f(a, b))
      case (Left(l), _) => Left(l)
      case (_, Left(l)) => Left(l)

    def unit[A](a: A): Either[L, A] = Right(a)

@main def runApplicativeDemo =
  import Applicative.given

  val o1 = Some(1)
  val o2 = None
  val o3 = Some(3)
  val o4 = Some(4)

  println {
    Applicative[Option].map4(o1, o2, o3, o4) { (el1, el2, el3, el4) => s"$el1-$el2-$el3-$el4" }
  }

  println {
    Applicative[Option].map3(o1, o3, o4) { (el1, el3, el4) => s"$el1-$el3-$el4" }
  }

@main def runApplicativeSequenceDemo =
  import Applicative.*

  val listOfOptions: List[Option[Int]] = (1 to 10).map(Some.apply).toList
  val listOfOptionsWithNone: List[Option[Int]] = List(Some(1), None, Some(3))

  println {
    Applicative[Option].sequence(listOfOptions)
  }
  println {
    Applicative[Option].sequence(listOfOptionsWithNone)
  }

  type EitherString[T] = Either[String, T]

  val listOfEithers: List[EitherString[Int]] = List(Right(1), Right(2), Right(3))
  val listOfEithersWithLeft: List[EitherString[Int]] = List(Left("one"), Right(2), Left("three"))

  println {
    Applicative[EitherString].sequence(listOfEithers)
  }
  println {
    Applicative[EitherString].sequence(listOfEithersWithLeft)
  }

@main def runApplicativeTraverseDemo =
  import Applicative.*

  type EitherNFE[T] = Either[NumberFormatException, T]

  def parseIntEither(s: String): EitherNFE[Int] =
    try Right(s.toInt)
    catch case ex: NumberFormatException => Left(ex)

  println {
    Applicative[EitherNFE].traverse(List("1", "2", "3"))(parseIntEither)
  }

  println {
    Applicative[EitherNFE].traverse(List("1", "abc", "3"))(parseIntEither)
  }
