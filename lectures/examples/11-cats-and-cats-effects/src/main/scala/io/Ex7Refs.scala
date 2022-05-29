package io

import cats.effect.{IO, IOApp, Ref, Sync}
import cats.syntax.all.*

class Worker[F[_]](number: Int, ref: Ref[F, Int])(implicit F: Sync[F]):
  private def putStrLn(value: String): F[Unit] = F.delay(println(value))

  def start: F[Unit] =
    for
      c1 <- ref.get
      _  <- putStrLn(show"#$number >> $c1")
      c2 <- ref.modify(x => (x + 1, x))
      _  <- putStrLn(show"#$number >> $c2")
    yield ()

val program: IO[Int] =
  for
    ref <- Ref[IO].of(0)
    w1  = new Worker[IO](1, ref)
    w2  = new Worker[IO](2, ref)
    w3  = new Worker[IO](3, ref)
    _   <- List(
             w1.start,
             w2.start,
             w3.start
           ).parSequence.void
    refFinal <- ref.get
  yield refFinal

object Ex07Refs extends IOApp.Simple:
  def run: IO[Unit] = program >>= IO.println
