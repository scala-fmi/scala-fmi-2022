package cats

import cats.implicits.*
//import cats.syntax.either.*
//import cats.syntax.option.*
//import cats.syntax.validated.*

@main def runDataTypesSyntax =
  // Option
  val maybeOne = 1.some
  val maybeN = none[Int]

  val either = maybeOne.toRightNec("It's not there :(")
  val validated = maybeOne.toValidNec("It's not there :(")

  val integer = maybeN.orEmpty

  // Validated

  val validatedOne = 1.validNec
  val validatedN = "Error".invalidNec

  validatedN

  validatedOne.toEither

  // Either

  val eitherOne = 1.asRight
  val eitherN = "Error".asLeft

  val eitherOneChain = 1.rightNec
  val eitherNChain = "Error".leftNec

  val recoveredEither = eitherN.recover { case "Error" =>
    42.asRight
  }

  eitherOneChain.toValidated
