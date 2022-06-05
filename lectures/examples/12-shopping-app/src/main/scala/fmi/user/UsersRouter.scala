package fmi.user

import cats.effect.IO
import fmi.utils.{AdtEntryCodec, CirceUtils}
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import org.http4s.dsl.io.*
import org.http4s.{AuthedRoutes, HttpRoutes}

class UsersRouter(usersService: UsersService, authorizationUtils: AuthenticationUtils):
  import UsersJsonCodecs.given
  import org.http4s.circe.CirceEntityCodec.given

  def nonAuthenticatedRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "users" =>
      for
        registrationForm <- req.as[UserRegistrationForm]
        maybeUser <- usersService.registerUser(registrationForm)
        response <- maybeUser.fold(errors => BadRequest(errors), _ => Ok())
      yield response

    case req @ POST -> Root / "login" =>
      for
        userLogin <- req.as[UserLogin]
        maybeUser <- usersService.login(userLogin)
        response <- maybeUser
          .map { user => authorizationUtils.responseWithUser(user.id) }
          .getOrElse(Forbidden())
      yield response
  }

  def authenticatedRoutes: AuthedRoutes[AuthenticatedUser, IO] = AuthedRoutes.of[AuthenticatedUser, IO] {
    case GET -> Root / "user" as user => Ok(user)
    case req @ POST -> Root / "logout" as _ => authorizationUtils.removeUser
  }

object UsersJsonCodecs:
  given Codec[UserRegistrationForm] = deriveCodec

  given Codec[UserId] = CirceUtils.unwrappedCodec(UserId.apply)(_.email)
  given Codec[UserLogin] = deriveCodec
  given Codec[UserRole] = CirceUtils.enumCodec(UserRole.valueOf)(_.toString)
  given Codec[AuthenticatedUser] = deriveCodec

  given Codec[RegistrationFormError] =
    CirceUtils.adtCodec[RegistrationFormError]("type")(
      AdtEntryCodec(deriveCodec[InvalidEmail], classOf),
      AdtEntryCodec(deriveCodec[NameIsEmpty.type], classOf),
      AdtEntryCodec(deriveCodec[InvalidAge], classOf)
    )
  given Codec[RegistrationError] =
    CirceUtils.adtCodec[RegistrationError]("type")(
      AdtEntryCodec(deriveCodec[UserValidationError], classOf),
      AdtEntryCodec(deriveCodec[UserAlreadyExists], classOf)
    )
