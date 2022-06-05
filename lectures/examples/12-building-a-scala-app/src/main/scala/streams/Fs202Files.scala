package streams

import cats.effect.{IO, IOApp}
import fs2.io.file.Files
import fs2.io.file.Path
import fs2.{Stream, text}

import java.nio.file.Paths

import cats.syntax.all.*

object Fs202Files extends IOApp.Simple:
  def converter(inputFile: String, outputFile: String): Stream[IO, String] =
    def fahrenheitToCelsius(f: Double): Double =
      (f - 32.0) * (5.0 / 9.0)

    Files[IO]
      .readAll(Path(inputFile))
      .through(text.utf8.decode)
      .through(text.lines)
      .filter(s => s.trim.nonEmpty && !s.startsWith("//"))
      .map(line => fahrenheitToCelsius(line.toDouble).toString)
      .intersperse("\n")
      .through(text.utf8.encode)
      .through(Files[IO].writeAll(Path(outputFile)))

  def run: IO[Unit] = converter("fahrenheit.txt", "celsius.txt").compile.toList >>= IO.println
