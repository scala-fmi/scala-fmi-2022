package streams

import cats.effect.{IO, IOApp}
import fs2.Stream
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.server.Router
import sql.{Country, DoobieApp}
import doobie.*
import doobie.implicits.*

import scala.concurrent.duration.DurationInt

case class City(name: String, country: String, population: Int)

object Fs204HttpWithDoobie extends IOApp.Simple:
  val allCities =
    sql"""
         SELECT name, countrycode, population
         FROM city
    """
      .query[City]
      .stream
      .transact(DoobieApp.dbTransactor)

  val count: Stream[IO, String] =
    Stream
      .awakeEvery[IO](100.millis)
      .map(_.toString + "\n")

  val routes = HttpRoutes.of[IO] {
    case GET -> Root / "counter" =>
      Ok(count.take(10))
    case GET -> Root / "cities" =>
      Ok(allCities.map(_.toString + "\n"))
    case GET -> Root / "combined" =>
      val output = (count zip allCities).map { case (elapsedTime, city) =>
        elapsedTime.toString + city.toString + "\n"
      }

      Ok(output)
  }

  val httpApp = routes.orNotFound

  val serverBuilder = BlazeServerBuilder[IO]
    .bindHttp(8080, "localhost")
    .withHttpApp(httpApp)
    .resource

  def run: IO[Unit] = serverBuilder.use(_ => IO.never)
