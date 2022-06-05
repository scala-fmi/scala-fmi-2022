package fmi

import cats.effect.kernel.Resource
import cats.effect.{IO, IOApp}
import cats.syntax.all.*
import fmi.server.LibraryHttpApp
import org.http4s.blaze.server.BlazeServerBuilder

object LibraryServer extends IOApp.Simple:
  val server =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(LibraryHttpApp.libraryApp)
      .resource

  def run: IO[Unit] =
    server
      .use(_ => IO.never)
      .onCancel(IO.println("Bye, see you again \uD83D\uDE0A"))
