package concurrent.future

import java.util.concurrent.Executor
import concurrent.Executors
import util.Utils

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Awaitable, CanAwait}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

trait Future[+A] extends Awaitable[A]:
  def value: Option[Try[A]]
  def onComplete(handler: Try[A] => Unit)(using ex: Executor): Unit

  def isComplete: Boolean = value.isDefined

  def map[B](f: A => B)(using ex: Executor): Future[B] =
    val p = Promise[B]
    onComplete(value => p.complete(value.map(f)))
    p.future

  def flatMap[B](f: A => Future[B])(using ex: Executor): Future[B] =
    val p = Promise[B]
    onComplete {
      case Success(value) => Future.tryF(f(value)).onComplete(p.complete)
      case Failure(e) => p.fail(e)
    }
    p.future

  def zip[B](fb: Future[B]): Future[(A, B)] =
    given Executor = Executors.currentThreadExecutor

    for
      a <- this
      b <- fb
    yield (a, b)

  def zipMap[B, R](fb: Future[B])(f: (A, B) => R)(using ex: Executor): Future[R] = zip(fb).map(f.tupled)

  def filter(f: A => Boolean)(using ex: Executor): Future[A] =
    val p = Promise[A]
    onComplete(value => p.complete(value.filter(f)))
    p.future

  def withFilter(f: A => Boolean)(using ex: Executor): Future[A] = filter(f)

  def foreach(f: A => Unit)(using ex: Executor): Unit = onComplete(_.foreach(f))

  def recover[B >: A](f: PartialFunction[Throwable, B])(using ex: Executor): Future[B] =
    val p = Promise[B]
    onComplete(value => p.complete(value.recover(f)))
    p.future

  def recoverWith[B >: A](f: PartialFunction[Throwable, Future[B]])(using ex: Executor): Future[B] =
    val p = Promise[B]
    onComplete {
      case Success(value) => p succeed value
      case Failure(e) =>
        if f.isDefinedAt(e) then Future.tryF(f(e)) onComplete { p complete _ }
        else p.fail(e)
    }
    p.future

object Future:
  def apply[A](value: => A)(using ex: Executor): Future[A] =
    val p = Promise[A]
    ex.execute(() => p.succeed(value))
    p.future
  def successful[A](value: A): Future[A] = resolved(Success(value))
  def failed[A](e: Throwable): Future[Nothing] = resolved(Failure(e))

  def resolved[A](r: Try[A]): Future[A] = new Future[A]:
    val value: Option[Try[A]] = Some(r)
    def onComplete(handler: Try[A] => Unit)(using ex: Executor): Unit = ex.execute(() => handler(r))

    def ready(atMost: Duration)(using permit: CanAwait): this.type = this
    def result(atMost: Duration)(using permit: CanAwait): A = r.get

  def tryF[A](f: => Future[A]): Future[A] =
    try f
    catch case NonFatal(e) => Future.failed(e)

//object FutureApp extends App:
//  import Executors.given Executor
//
//  def calc1 = Future {
//    1 + 1
//  }
//
//  def calc2 = Future {
//    42
//  }
//
//  def double(n: Int) = Future {
//    n * 2
//  }
//
//  val combinedCalculation =
//    for
//      n <- Future.successful(10)
//      (r1, r2) <- calc1 zip calc2
//      doubled <- double(r1 + r2)
//    yield doubled + n
//
//  combinedCalculation.foreach(println)
//
//  Await.result(combinedCalculation, Duration.Inf)
//
////  Thread.sleep(5000)
