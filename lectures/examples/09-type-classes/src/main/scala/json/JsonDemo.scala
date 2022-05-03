package json

import JsonSerializable.given
import JsonValue.*

@main def runJsonDemo =
  List(1, 2, 3).toJsonString // [1, 2, 3]

  val ivan = Person("Ivan", "ivan@abv.bg", 23)
  val georgi = Person("Georgi", "georgi@gmail.bg", 28)

  ivan.toJson // JsonObject(Map(name -> JsonString(Ivan), email -> JsonString(ivan@abv.bg), age -> JsonNumber(23)))
  ivan.toJsonString // {"name": "Ivan", "email": "ivan@abv.bg", "age": 23}

  // Person's JsonSerializable composes with List's JsonSerializable
  List(
    ivan,
    georgi
  ).toJsonString // [{"name": "Ivan", "email": "ivan@abv.bg", "age": 23}, {"name": "Georgi", "email": "georgi@abv.bg", "age": 28}]

  {
    // We might want to skip some fields, like email, for example because we don't want to share
    // user's email with other users.
    // It's really easy to do so with this implementation -
    // just provide another Person's JsonSerializable in this context

    given JsonSerializable[Person] with
      // An utility can be created to even more easily create JsonObject
      def toJsonValue(person: Person): JsonValue = JsonObject(
        Map(
          "name" -> person.name.toJson,
          "age" -> person.age.toJson
        )
      )

    // It will compose just as easily
    println {
      List(ivan, georgi).toJsonString // [{"name": "Ivan", "age": 23}, {"name": "Georgi", "age": 28}]
    }
  }

  // We can implement this for deserialization, too. circe and play-json are libraries that implements this pattern
  // and provide many utilities to ease the creation of (de)serializers.
  // Since the mapping depends on only compile-time information the implementation is faster than e.g. Jackson (which uses reflection)
