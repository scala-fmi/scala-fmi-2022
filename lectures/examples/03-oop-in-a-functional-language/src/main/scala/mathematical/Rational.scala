package mathematical

class Rational(n: Int, d: Int = 1) extends Ordered[Rational]:
  require(d != 0)

  val (numer, denom) =
    val div = gcd(n, d)
    ((n / div) * d.sign, (d / div).abs)

  def compare(that: Rational): Int = (this - that).numer

  def unary_- = Rational(-numer, denom)

  def unary_~ = Rational(denom, numer)

  def +(that: Rational) = Rational(
  numer * that.denom + that.numer * denom,
  denom * that.denom
  )

  def -(that: Rational) = this + (-that)

  def *(that: Rational) = Rational(numer * that.numer, denom * that.denom)

  def /(that: Rational) = this * (~that)

  override def toString: String = s"$numer/$denom"

  override def hashCode(): Int = (numer, denom).##

  override def equals(obj: Any): Boolean = obj match
  case that: Rational => numer == that.numer && denom == that.denom
  case _ => false

  private def gcd(a: Int, b: Int): Int = if b == 0 then a else gcd(b, a % b)

object Rational:
  val Zero = Rational(0) // използва apply, дефиниран долу

  //  def apply(n: Int, d: Int = 1) = new Rational(n, d)

  implicit def intToRational(n: Int): Rational = Rational(n)

  def sum(rationals: Rational*): Rational =
  if rationals.isEmpty then Zero
  else rationals.head + sum(rationals.tail*)