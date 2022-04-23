package concurrent.future

import concurrent.Executors

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.LockSupport
import scala.annotation.tailrec
import scala.concurrent.duration.Duration
import scala.concurrent.{CanAwait, TimeoutException}
import scala.util.{Failure, Success, Try}

// An alternative implementation using synchronized
class PromiseAlternativeImplementation[A]:
  case class Handler(handler: Try[A] => Any, ex: Executor):
    def executeWithValue(value: Try[A]): Unit = ex.execute(() => handler(value))

  sealed trait State
  case class Completed(value: Try[A]) extends State
  case class Pending(handlers: List[Handler]) extends State

  private var state: State = Pending(List.empty)

  private def executeWhenComplete(handler: Handler): Unit = this.synchronized {
    state match
      case Completed(value) => handler.executeWithValue(value)
      case s @ Pending(handlers) => state = Pending(handler :: handlers)
  }

  private def completeWithValue(value: Try[A]): List[Handler] = this.synchronized {
    state match
      case Completed(_) => List.empty
      case s @ Pending(handlers) =>
        state = Completed(value)
        handlers
  }

  val future: Future[A] = new Future[A]:
    def value: Option[Try[A]] = this.synchronized {
      state match
        case Completed(value) => Some(value)
        case _ => None
    }

    def onComplete(handler: Try[A] => Unit)(using ex: Executor): Unit = executeWhenComplete(Handler(handler, ex))

    // Blocks the current thread until the result is ready
    def ready(atMost: Duration)(using permit: CanAwait): this.type =
      if !isComplete && Duration.Zero < atMost then
        val thread = Thread.currentThread
        onComplete(_ => LockSupport.unpark(thread))(using Executors.currentThreadExecutor)

        if atMost == Duration.Inf then LockSupport.park()
        else LockSupport.parkNanos(atMost.toNanos)

      if isComplete then this
      else throw new TimeoutException

    def result(atMost: Duration)(using permit: CanAwait): A = ready(atMost).value.get.get

  def complete(value: Try[A]): PromiseAlternativeImplementation[A] =
    completeWithValue(value).foreach(_.executeWithValue(value))
    this

  def succeed(value: A): PromiseAlternativeImplementation[A] = complete(Success(value))

  def fail(e: Throwable): PromiseAlternativeImplementation[A] = complete(Failure(e))
