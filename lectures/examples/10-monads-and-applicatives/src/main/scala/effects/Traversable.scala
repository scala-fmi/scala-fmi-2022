package effects

import validation.RegistrationData

trait Traversable[F[_]] extends Functor[F]:
  def traverse[G[_] : Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
  // if sequnce is abstract traverse can be implemented as:
  // sequence(map(fa)(f))

  def sequence[G[_] : Applicative, A](fga: F[G[A]]): G[F[A]] =
    traverse(fga)(ga => ga)

  extension [A](fa: F[A])
    def map[B](f: A => B): F[B] =
      type Id[A] = A

      val idApplicative = new Applicative[Id]:
        def map2[A, B, C](fa: Id[A], fb: Id[B])(f: (A, B) => C): Id[C] = f(fa, fb)
        def unit[A](a: => A): Id[A] = a

      traverse[Id, A, B](fa)(f)(idApplicative)

object Traversable:
  def apply[F[_]](using a: Traversable[F]): Traversable[F] = a

  given Traversable[List] with
    def traverse[G[_], A, B](as: List[A])(f: A => G[B])(implicit G: Applicative[G]): G[List[B]] =
      as.foldRight(G.unit(List[B]()))((a, fbs) => G.map2(f(a), fbs)(_ :: _))

  given Traversable[Option] with
    def traverse[G[_], A, B](oa: Option[A])(f: A => G[B])(implicit G: Applicative[G]): G[Option[B]] =
      oa match
        case Some(a) => G.map(f(a))(Some(_))
        case None => G.unit(None)

  case class Tree[+A](head: A, tail: List[Tree[A]])
  given Traversable[Tree] with
    def traverse[M[_], A, B](ta: Tree[A])(f: A => M[B])(implicit M: Applicative[M]): M[Tree[B]] =
      M.map2(f(ta.head), Traversable[List].traverse(ta.tail)(a => traverse(a)(f)))(Tree(_, _))

@main def runTraversableDemo =
  import Traversable.*
  import Applicative.given Applicative[Option]
  import validation.FormValidatorNecApplicative
  import validation.FormValidatorNecApplicative.ValidationResult
  import validation.FormValidatorNecApplicative.given Applicative[ValidationResult]

  val listOfOptions: List[Option[Int]] = List(Some(1), Some(2), Some(3))

  println {
    Traversable[List].sequence(listOfOptions)
  }

  println {
    val optionOfValidated: Option[ValidationResult[RegistrationData]] = Some(
      FormValidatorNecApplicative.validateForm(
        username = "fake$Us#rname",
        password = "password"
      )
    )

    Traversable[Option].sequence(optionOfValidated)
  }

  println {
    val optionOfValidated: Option[ValidationResult[RegistrationData]] = Some(
      FormValidatorNecApplicative.validateForm(
        username = "correctUsername",
        password = "Password123#"
      )
    )

    Traversable[Option].sequence(optionOfValidated)
  }

  println {
    val optionOfValidated: Option[ValidationResult[RegistrationData]] = None

    Traversable[Option].sequence(optionOfValidated)
  }
