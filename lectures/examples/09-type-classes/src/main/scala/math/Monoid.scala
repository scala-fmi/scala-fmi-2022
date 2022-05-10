package math

trait Monoid[A]:
  extension (a: A)
    def |+|(b: A): A
  def identity: A

object Monoid:
  def apply[A](using m: Monoid[A]): Monoid[A] = m

  given Monoid[Int] with
    extension (a: Int) def |+|(b: Int): Int = a + b
    val identity: Int = 0

  val intMultiplicativeMonoid: Monoid[Int] = new Monoid[Int]:
    extension (a: Int) def |+|(b: Int): Int = a * b
    val identity: Int = 1

  given Monoid[String] with
    extension (a: String) def |+|(b: String): String = a + b
    val identity: String = ""

  given [A: Monoid]: Monoid[Option[A]] with
    extension (a: Option[A]) def |+|(b: Option[A]): Option[A] = (a, b) match
      case (Some(a), Some(b)) => Some(a |+| b)
      case (Some(_), _) => a
      case (_, Some(_)) => b
      case _ => None

    val identity: Option[A] = None

  given [A : Monoid, B : Monoid]: Monoid[(A, B)] with
    extension (p1: (A, B)) def |+|(p2: (A, B)): (A, B) = (p1, p2) match
      case ((a1, b1), (a2, b2)) => (a1 |+| a2, b1 |+| b2)

    def identity: (A, B) = (Monoid[A].identity, Monoid[B].identity)

  given [K, V : Monoid]: Monoid[Map[K, V]] with
    extension (a: Map[K, V]) def |+|(b: Map[K, V]): Map[K, V] =
      val vIdentity = Monoid[V].identity

      (a.keySet ++ b.keySet).foldLeft(identity) { (acc, key) =>
        acc + (key -> (a.getOrElse(key, vIdentity) |+| b.getOrElse(key, vIdentity)))
      }

    def identity: Map[K, V] = Map.empty[K, V]