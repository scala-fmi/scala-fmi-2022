package json

import JsonValue.JsonObject

import JsonSerializable.given

case class Person(name: String, email: String, age: Int)

object Person:
  given JsonSerializable[Person] with
    def toJsonValue(person: Person): JsonValue = JsonObject(
      Map(
        "name" -> person.name.toJson,
        "email" -> person.email.toJson,
        "age" -> person.age.toJson
      )
    )
