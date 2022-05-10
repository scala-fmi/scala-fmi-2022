package answers

trait Semigroup[M]:
  extension (a: M) def |+|(b: M): M

object Semigroup:
  def apply[A](using m: Semigroup[A]): Semigroup[A] = m
