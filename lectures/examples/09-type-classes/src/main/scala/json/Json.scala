package json

import scala.annotation.targetName
import scala.language.implicitConversions

enum JsonValue:
  case JsonNumber(value: BigDecimal)
  case JsonString(value: String)
  case JsonBoolean(value: Boolean)
  case JsonArray(value: Seq[JsonValue])
  case JsonObject(value: Map[String, JsonValue])
  case JsonNull

object JsonValue:
  def toString(json: JsonValue): String = json match
    case JsonNumber(value) => value.toString
    case JsonString(value) => s"""\"$value\""""
    case JsonBoolean(value) => value.toString
    case JsonArray(elements) => "[" + elements.map(toString).mkString(", ") + "]"
    case JsonObject(members) =>
      val membersStrings = members.map { case (key, value) =>
        s"""  \"$key\": ${toString(value)}"""
      }
      "{\n" + membersStrings.mkString(",\n") + "\n}"
    case JsonNull => "null"

trait JsonSerializable[A]:
  def toJsonValue(a: A): JsonValue

  extension (a: A)
    def toJson: JsonValue = toJsonValue(a)
    def toJsonString: String = JsonValue.toString(toJsonValue(a))

object JsonSerializable:
  import JsonValue.*

  given JsonSerializable[Int] with
    def toJsonValue(a: Int): JsonValue = JsonNumber(a)

  given JsonSerializable[String] with
    def toJsonValue(a: String): JsonValue = JsonString(a)

  given JsonSerializable[Boolean] with
    def toJsonValue(a: Boolean): JsonValue = JsonBoolean(a)

  given [A : JsonSerializable]: JsonSerializable[Option[A]] with
    def toJsonValue(opt: Option[A]): JsonValue = opt match
      case Some(a) => a.toJson
      case _ => JsonNull

  given [A : JsonSerializable]: JsonSerializable[List[A]] with
    def toJsonValue(a: List[A]): JsonValue = JsonArray(
      a.map(value => value.toJson)
    )
