package http

import cats.effect.IO
import cats.implicits.*
import http.Utils.*
import org.http4s.HttpRoutes
import org.http4s.dsl.io.*
import org.http4s.implicits.*

import java.time.LocalDate
import scala.util.Try


@main
def run =

  // Match rest of the path
  val app = HttpRoutes.of[IO] { case GET -> "hello" /: rest =>
    Ok(s"""Rest of the path is: $rest""")
  }

  println(requestEntityUnsafe[String](app)(get(uri"hello/rest/of/the/path")))



  def getUserName(userId: Int): IO[String] = IO.pure(s"User$userId")

  val usersService = HttpRoutes.of[IO] {
    case GET -> Root / "users" / IntVar(userId) =>
      Ok(getUserName(userId))
    case GET -> Root / "usersLong" / LongVar(_) =>
      ???
    case GET -> Root / "usersUUID" / UUIDVar(_) =>
      ???
  }

  println(requestEntityUnsafe[String](usersService)(get(uri"/users/1")))
  println(requestUnsafe(usersService)(get(uri"/users/two")))


  /** Custom extractors We need: def unapply(str: String): Option[T]
    */

  object LocalDateVar:
    def unapply(str: String): Option[LocalDate] =
      if str.nonEmpty then Try(LocalDate.parse(str)).toOption
      else None

  def getTemperatureForecast(date: LocalDate): IO[Double] = IO.println(date) *> IO(42.23)

  val dailyWeatherService = HttpRoutes.of[IO] {
    case GET -> Root / "weather" / "temperature" / LocalDateVar(localDate) =>
      Ok(getTemperatureForecast(localDate).map(s"The temperature on $localDate will be: " + _))
  }

  println(requestEntityUnsafe[String](dailyWeatherService)(get(uri"/weather/temperature/2016-11-05")))
