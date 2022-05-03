package math

trait Semigroup[M]:
  def op(a: M, b: M): M

  extension (a: M) def |+|(b: M): M = op(a, b)

object Semigroup:
  def apply[A](using m: Semigroup[A]): Semigroup[A] = m
