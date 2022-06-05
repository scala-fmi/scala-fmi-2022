package sql

import cats.effect.{IO, IOApp}
import cats.syntax.flatMap.*
import doobie.*
import doobie.implicits.*

object DoobieApp extends IOApp.Simple:
  val dbTransactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql:world",
    "postgres",
    "password"
  )

  def run: IO[Unit] = Doobie03Fragments.ex2.transact(dbTransactor) >>= IO.println
