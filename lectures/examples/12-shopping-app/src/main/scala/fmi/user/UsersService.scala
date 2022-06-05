package fmi.user

import cats.data.{EitherT, NonEmptyChain}
import cats.effect.IO
import cats.syntax.all.*

class UsersService(usersDao: UsersDao):
  def registerUser(registrationForm: UserRegistrationForm): IO[Either[RegistrationError, User]] = (for
    user <- EitherT.fromEither[IO](
      UserRegistrationForm
        .validate(registrationForm)
        .leftMap(UserValidationError.apply)
        .leftWiden[RegistrationError]
    )
    _ <- EitherT(usersDao.registerUser(user)).leftWiden[RegistrationError]
  yield user).value

  def login(userLogin: UserLogin): IO[Option[User]] =
    usersDao.retrieveUser(userLogin.email).map {
      case Some(user) =>
        if PasswordUtils.checkPasswords(userLogin.password, user.passwordHash) then Some(user)
        else None
      case _ => None
    }

sealed trait RegistrationError
case class UserValidationError(registrationErrors: NonEmptyChain[RegistrationFormError]) extends RegistrationError
case class UserAlreadyExists(email: UserId) extends RegistrationError

case class UserLogin(email: UserId, password: String)
