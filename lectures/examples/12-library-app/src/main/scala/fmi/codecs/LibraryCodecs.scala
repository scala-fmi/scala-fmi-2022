package fmi.codecs

import fmi.library.{Author, AuthorId, Book, BookId, BookSummary}
import io.circe.Codec
import io.circe.Encoder
import io.circe.Decoder

object LibraryCodecs:
  import io.circe.generic.semiauto.*

  given Codec[String] = Codec.from(Decoder.decodeString, Encoder.encodeString)

  given Codec[AuthorId] = Codec[String].iemap(id => Right(AuthorId(id)))(_.id)
  given Codec[Author] = deriveCodec

  given Codec[BookId] = Codec[String].iemap(id => Right(BookId(id)))(_.id)
  given Codec[Book] = deriveCodec

  given Codec[BookSummary] = deriveCodec
