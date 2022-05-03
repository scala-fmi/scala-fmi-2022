package effects.state

import effects.Monad
import effects.state.State.*

case class RNG(seed: Long):
  def nextInt: (RNG, Int) =
    val newSeed = (seed * 0x5deece66dL + 0xbL) & 0xffffffffffffL
    val nextRNG = RNG(newSeed)
    val n = (newSeed >>> 16).toInt

    (nextRNG, n)

object RNG:
  val nextInt = State((rng: RNG) => rng.nextInt)
  val nextBoolean = nextInt.map(_ > 0)

@main def runRNGDemo =
  import RNG.*

  // Without State we have to do this:
  val (rng1, next1) = RNG(System.currentTimeMillis).nextInt
  val (rng2, next2) = rng1.nextInt
  val (rng3, next3) = rng2.nextInt

  println((next1, next2, next3))

  // With state
  val randomTuple = for
    a <- nextInt
    b <- nextInt
    c <- nextBoolean
    d <- nextBoolean
  yield (a, b, a + b, c)

  println(randomTuple.run(RNG(System.currentTimeMillis)))

  val b = nextInt.flatMap(i => nextBoolean)
