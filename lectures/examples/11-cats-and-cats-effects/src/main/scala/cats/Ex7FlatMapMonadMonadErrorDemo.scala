package cats

import cats.syntax.flatMap.*
import cats.syntax.monad.*
import cats.syntax.applicativeError.*
import cats.syntax.monadError.*
import cats.syntax.apply.*
import user.{Email, User}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

@main def runFlatMapMonadMonadErrorDemo =
  // FlatMap

  // flatten, flatMap, ...

  def verifyUser(user: User): Future[Boolean] = ???
  def acceptUser(user: User): Future[String] = ???
  def storeUserForReview(user: User): Future[String] = ???

  def registerUser(user: User): Future[String] =
    verifyUser(user).ifM(
      ifTrue = acceptUser(user),
      ifFalse = storeUserForReview(user)
    )
  val user = User("A", Email("a", "gmail.com"), "123")
  //  registerUser(user)

  // TODO: >> and >>= examples

  // Monad

  def asyncIncrement(n: Int) = Future {
    println(s"Contacting our incremental service to increment $n...")

    n + 1
  }

  val asyncCalculation = 1.iterateWhileM(asyncIncrement)(_ < 10)
  println(Await.result(asyncCalculation, 1.second))

  // ApplicativeError & MonadError
  // They add things like recoverWith, orElse, ...
