package fmi.utils

import io.circe.Decoder.Result
import io.circe.{Codec, Decoder, DecodingFailure, Encoder, HCursor, Json}

import scala.util.Try

case class AdtEntryCodec[A, E <: A](codec: Codec[E], clazz: Class[E]):
  def isApplicable(a: A) = clazz.isInstance(a)

  def apply(a: A): Option[Json] =
    if isApplicable(a) then
      val e = a.asInstanceOf[E]
      Some(codec(e))
    else None

  def getType = clazz.getSimpleName.stripSuffix("$")

object CirceUtils:
  val stringCodec = Codec.from(Decoder[String], Encoder[String])

  def unwrappedCodec[W, U : Encoder : Decoder](wrap: U => W)(unwrap: W => U): Codec[W] =
    Codec.from(Decoder[U], Encoder[U]).iemap(u => Right(wrap(u)))(unwrap)

  def enumCodec[E](valueOf: String => E)(toString: E => String): Codec[E] =
    stringCodec.iemap(s => Try(valueOf(s)).toEither.left.map(_.getMessage))(toString)

  def adtCodec[A](typeField: String)(types: AdtEntryCodec[A, ? <: A]*): Codec[A] = new Codec[A]:
    override def apply(a: A): Json =
      val result = for
        entry <- types.find(_.isApplicable(a))
        encoded <- entry.apply(a)
      yield encoded.mapObject((typeField -> Json.fromString(entry.getType)) +: _)

      result.getOrElse(throw new RuntimeException(s"Unexpected ADT instance of type ${a.getClass}"))

    override def apply(c: HCursor): Result[A] = for
      typeField <- c.get[String](typeField)
      adtEntry <- types.find(_.getType == typeField).toRight(DecodingFailure.apply("", c.history))
      instance <- adtEntry.codec.apply(c)
    yield instance
