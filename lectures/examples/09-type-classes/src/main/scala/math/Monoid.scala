package math

trait Monoid[M] extends Semigroup[M]:
  def op(a: M, b: M): M // no need to repeat this one from Semigroup, it's here just for completeness
  def identity: M

object Monoid:
  def apply[A](using m: Monoid[A]): Monoid[A] = m

  given Monoid[Int] with
    def op(a: Int, b: Int): Int = a + b
    val identity: Int = 0

  val intMultiplicativeMonoid: Monoid[Int] = new Monoid[Int]:
    def op(a: Int, b: Int): Int = a * b
    val identity: Int = 1

  given Monoid[String] with
    def op(a: String, b: String): String = a + b
    val identity: String = ""

  given [A : Monoid]: Monoid[Option[A]] with
    def op(a: Option[A], b: Option[A]): Option[A] = (a, b) match
      case (Some(n), Some(m)) => Some(n |+| m)
      case (Some(_), _) => a
      case (_, Some(_)) => b
      case _ => None

    def identity: Option[A] = None

  given [A : Monoid, B : Monoid]: Monoid[(A, B)] with
    def op(a: (A, B), b: (A, B)): (A, B) = (a, b) match
      case ((a1, a2), (b1, b2)) => (a1 |+| b1, a2 |+| b2)

    def identity: (A, B) = (Monoid[A].identity, Monoid[B].identity)

  given [K, V : Monoid]: Monoid[Map[K, V]] with
    def op(a: Map[K, V], b: Map[K, V]): Map[K, V] =
      val vIdentity = Monoid[V].identity

      (a.keySet ++ b.keySet).foldLeft(identity) { (acc, key) =>
        acc + (key -> (a.getOrElse(key, vIdentity) |+| b.getOrElse(key, vIdentity)))
      }

    def identity: Map[K, V] = Map.empty[K, V]
