package validation

import cats.data.Validated.{Invalid, Valid}
import effects.Applicative
import validation.FormValidatorNecApplicative.ValidationResult

object FormValidatorNecApplicative:
  import cats.data.*
  import cats.data.Validated.*
  import cats.implicits.*

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  private def validateUserName(userName: String): ValidationResult[String] =
    if userName.matches("^[a-zA-Z0-9]+$") then userName.validNec else UsernameHasSpecialCharacters.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") then password.validNec
    else PasswordDoesNotMeetCriteria.invalidNec

  def validateForm(
    username: String,
    password: String
  )(using validatedApplicative: Applicative[ValidationResult]
  ): ValidationResult[RegistrationData] =
    validatedApplicative.map2(
      validateUserName(username),
      validatePassword(password)
    )(RegistrationData.apply)

  given Applicative[ValidationResult] with
    def map2[A, B, C](fa: ValidationResult[A], fb: ValidationResult[B])(f: (A, B) => C): ValidationResult[C] =
      (fa, fb) match
        case (Valid(a), Valid(b)) => Valid(f(a, b))
        case (Valid(_), Invalid(nec)) => Invalid(nec)
        case (Invalid(nec), Valid(_)) => Invalid(nec)
        case (Invalid(nec1), Invalid(nec2)) => Invalid(nec1 ++ nec2)

    def unit[A](a: => A): ValidationResult[A] = Valid(a)

@main def runFormValidatorNecApplicativeDemo =
  import FormValidatorNecApplicative.given Applicative[ValidationResult]

  println {
    FormValidatorNecApplicative.validateForm(
      username = "fake$Us#rname",
      password = "password"
    )
  }
