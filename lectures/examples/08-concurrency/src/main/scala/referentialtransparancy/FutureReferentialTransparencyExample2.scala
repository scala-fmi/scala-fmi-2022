package referentialtransparancy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

@main def runFutureReferentialTransparencyExample2Variant1 =
  def calc[T](expr: => T) = Future {
    println("Hello")

    Thread.sleep(4000)

    expr
  }

  val futureCalc = calc(42)

  // "Hello" will be printed once
  val sum = for
    (a, b) <- futureCalc zip futureCalc
  yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
end runFutureReferentialTransparencyExample2Variant1

@main def runFutureReferentialTransparencyExample2Variant2 =
  def calc[T](expr: => T) = Future {
    println("Hello")

    Thread.sleep(4000)

    expr
  }

  // "Hello" will be printed twice
  // To fix this we will introduce an alternative implementation to Future called (asynchronous) IO
  val sum = for
    (a, b) <- calc(42) zip calc(42)
  yield a + b

  println {
    Await.result(sum, 5.seconds)
  }
end runFutureReferentialTransparencyExample2Variant2
