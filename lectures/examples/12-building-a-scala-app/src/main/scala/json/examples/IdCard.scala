package json.examples

import io.circe.Decoder.Result
import io.circe.syntax.*
import io.circe.{Decoder, Encoder, HCursor, Json, ParsingFailure}
import io.circe.parser.*

case class Person(name: String, age: Int)
case class IdCard(id: String, person: Person)

object IdCard:
  given Encoder[IdCard] with
    def apply(a: IdCard): Json = Json.obj(
      "id" -> a.id.asJson,
      "person" -> a.person.asJson
    )

  given Decoder[IdCard] with
    def apply(c: HCursor): Result[IdCard] = for
      id <- c.get[String]("id")
      person <- c.get[Person]("person")
    yield IdCard(id, person)

object Person:
  given Encoder[Person] with
    def apply(a: Person): Json = Json.obj(
      "name" -> a.name.asJson,
      "age" -> a.age.asJson
    )

  given Decoder[Person] with
    def apply(c: HCursor): Result[Person] = for
      name <- c.get[String]("name")
      age <- c.get[Int]("age")
    yield Person(name, age)

object EncodersExample extends App:
  val idCard = IdCard("6937745", Person("Viktor Marinov", 25))

  println(idCard.asJson.spaces2)

  println(idCard.asJson.spaces4)
  println(idCard.asJson.noSpaces)
  println(idCard.asJson.spaces2SortKeys)

  val idCard2 = IdCard("37842634", Person("Zdravko", 21))
  println(List(idCard, idCard2).asJson)

  val maybeIdCard: Option[IdCard] = Some(idCard)
  val maybeIdCard2: Option[IdCard] = None
  println(maybeIdCard.asJson)
  println(maybeIdCard2.asJson)

object DecodersExample extends App:
  val json =
    """{
      |  "id" : "6937745",
      |  "person" : {
      |    "name" : "Viktor Marinov",
      |    "age" : 25
      |  }
      |}""".stripMargin

  println(decode[IdCard](json))

  val wrongJson =
    """{
      |  "id" : "6937745",
      |  "person" : {
      |    "name" : "Viktor Marinov"
      |  }
      |}""".stripMargin

  println(decode[IdCard](wrongJson))

object ParserExample extends App:
  val json =
    """{
      |  "id" : "6937745",
      |  "person" : {
      |    "name" : 23
      |    "age" : 25
      |  }
      |}""".stripMargin

  val parseResult: Either[ParsingFailure, Json] = parse(json)
  println(parseResult)
