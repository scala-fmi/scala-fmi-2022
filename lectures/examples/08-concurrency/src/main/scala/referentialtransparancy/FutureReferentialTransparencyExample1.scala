package referentialtransparancy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

@main def runFutureReferentialTransparencyExample1Variant1 =
  def calc[T](expr: => T) = Future {
    Thread.sleep(4000)

    expr
  }

  val futureA = calc(42)
  val futureB = calc(10)

  val sum = for
    a <- futureA
    b <- futureB
  yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
end runFutureReferentialTransparencyExample1Variant1

// This one, unlike the one above, will timeout
@main def runFutureReferentialTransparencyExample1Variant2 =
  def calc[T](expr: => T) = Future {
    Thread.sleep(4000)

    expr
  }

  val sum = for
    a <- calc(42)
    b <- calc(42)
  yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
end runFutureReferentialTransparencyExample1Variant2

@main def runFutureReferentialTransparencyExample1HowToWriteItCorrectly =
  def calc[T](expr: => T) = Future {
    Thread.sleep(4000)

    expr
  }

  // if you want calculations executed concurrently you must use zip
  val sum = for
    (a, b) <- calc(42) zip calc(42)
  yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
end runFutureReferentialTransparencyExample1HowToWriteItCorrectly
