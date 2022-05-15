package validation

import effects.{Monad as MyMonad}

object FormValidatorNec:
  import cats.data.*
  import cats.data.Validated.*
  import cats.implicits.*

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  private def validateUserName(userName: String): ValidationResult[String] =
    if userName.matches("^[a-zA-Z0-9]+$") then userName.validNec else UsernameHasSpecialCharacters.invalidNec

  private def validatePassword(password: String): ValidationResult[String] =
    if password.matches("(?=^.{10,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$") then password.validNec
    else PasswordDoesNotMeetCriteria.invalidNec

  given MyMonad[ValidationResult] with
    def unit[A](a: A): ValidationResult[A] = Valid(a)

    extension [A](fa: ValidationResult[A])
      def flatMap[B](f: A => ValidationResult[B]): ValidationResult[B] = fa match
        case Valid(a) => f(a)
        case i @ Invalid(_) => i

  def validateForm(username: String, password: String): ValidationResult[RegistrationData] =
    MyMonad[ValidationResult].map2(
      validateUserName(username),
      validatePassword(password)
    )(RegistrationData.apply)

@main def runFormValidatorNecDemo =
  println {
    FormValidatorNec.validateForm(
      username = "fake$Us#rname",
      password = "password"
    )
  }
