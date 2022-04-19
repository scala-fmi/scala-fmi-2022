package concurrent.lecture

import concurrent.Executors
import product.ProductFactory.produceProduct

import java.util.concurrent.Executor
import scala.util.Try

trait Future[A]:
  def value: Option[Try[A]]

  def onComplete(f: Try[A] => Unit)(using ex: Executor): Unit

  def isCompleted: Boolean = value.isDefined

  def map[B](f: A => B)(using ex: Executor): Future[B]
  def flatMap[B](f: A => Future[B])(using ex: Executor): Future[B]

  def zip[B](other: Future[B])(using ex: Executor): Future[(A, B)]
  def zipMap[B, C](other: Future[B])(f: (A, B) => C)(using ex: Executor): Future[C]

  // filter, transform,

  // see concurrent.future for complete implementation

object Future:
  def apply[A](value: => A)(using ex: Executor): Future[A] = ???

  import Executors.given Executor

  Future(produceProduct("Book"))
    .map(_.name)
