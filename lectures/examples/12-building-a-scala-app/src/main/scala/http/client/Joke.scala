package http.client

import cats.effect.IO
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

import io.circe.Codec
import io.circe.parser.*
import io.circe.syntax.*

case class Joke(joke: String) extends AnyVal

object Joke:
  import io.circe.generic.semiauto.*

//  given Codec[Joke] = deriveCodec[Joke]
