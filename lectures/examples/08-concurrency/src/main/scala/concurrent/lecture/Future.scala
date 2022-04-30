package concurrent.lecture

import concurrent.Executors
import product.ProductFactory
import product.ProductFactory.produceProduct
import util.Utils

import java.util.concurrent.{CompletableFuture, Executor, ForkJoinPool}
import scala.util.Try

trait Future[+A]:
  def value: Option[Try[A]]
  def isComplete: Boolean = value.nonEmpty

  def onComplete(callback: Try[A] => Unit)(using ex: Executor): Unit

  def map[B](f: A => B)(using ex: Executor): Future[B]
  def flatMap[B](f: A => Future[B])(using ex: Executor): Future[B]

  def zip[B](that: Future[B]): Future[(A, B)]
  def zipMap[B, R](that: Future[B])(f: (A, B) => R): Future[R]

  def filter(predicate: A => Boolean)(using ex: Executor): Future[A]

  def withFilter(f: A => Boolean)(using ex: Executor): Future[A] = filter(f)
  // sequence

object Future:
  def apply[A](result: => A)(using ex: Executor): Future[A] = ???

  given Executor = new ForkJoinPool()

  Future(ProductFactory.produceProduct("computer"))
    .map(_.name)

  def calc1 = Future {
    1 + 1
  }

  def calc2 = Future {
    42
  }

  def double(n: Int) = Future {
    n * 2
  }

  val combinedCalculation =
    for
      (r1, r2) <- calc1 zip calc2
      doubled <- double(r1 + r2)
    yield doubled