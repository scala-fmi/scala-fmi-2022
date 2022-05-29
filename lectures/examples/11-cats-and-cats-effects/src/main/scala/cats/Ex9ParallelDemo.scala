package cats

import cats.AlternativeUserRegistration.*
import cats.arrow.FunctionK
import cats.data.{EitherNec, ValidatedNec, ZipList}
import cats.instances.all.*
import cats.syntax.apply.*
import cats.syntax.either.*
import cats.syntax.parallel.*
import cats.syntax.validated.*
import user.Email

@main def runParallelDemo =
  FunctionK

  def registerUser(token: String)(name: String, email: String): EitherNec[String, User] = for
    token <- verifyUserToken(token)
    user <- (
      validateName(name),
      validateEmail(email)
    ).parMapN(User.apply)
  yield user

  println(registerUser("ssff")("", "boyanboyan.com"))

  println {
    (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)).mapN((x, y, z) => x + y + z)
  }
  println {
    (List(1, 2, 3), List(10, 20, 30), List(100, 200, 300)).parMapN((x, y, z) => x + y + z)
  }

object AlternativeUserRegistration:
  def verifyUserToken(token: String): EitherNec[String, String] =
    if token.length > 10 then token.rightNec
    else s"Invalid token: $token".leftNec

  def validateName(name: String): EitherNec[String, String] =
    if name.nonEmpty then name.rightNec
    else "Name is empty".leftNec

  def validateEmail(email: String): EitherNec[String, Email] = email match
    case Email(user, domain) => Email(user, domain).rightNec
    case _ => s"Email is invalid: $email".leftNec

case class User(name: String, email: Email)
