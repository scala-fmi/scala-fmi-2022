# Pattern Matching (Съпоставяне по образци)

## Възможни pattern-и

* Прости образци
  * всичко – `_`
     
    ```scala
    option match
      case Some(x) => x
      case _ => alternative // matches everything else
    ```
  * даване на име – `x`, `person` (всеки идентификатор, започващ с малка буква)
  
    ```scala
    list match
      case Nil => "An empty list"
      case nonEmptyList => s"A list with ${nonEmptyList.size} elements"
    ```
  * съпоставяне по тип – `s: String` (придава и име) или `_: Int` (проверява типа, но не деклалира променлива)
  
    ```scala
    value match
      case n: Int => n
      case s: String => s.toInt
      case d: Double => d.toInt
    ```
  * съпоставяне по константа/по обект – произволен литерал: `42`, `"abcdef"` и т.н., или константа: `Pi`, `NumberOfRetries`, `Nil` (т.е. идентификатор, започващ с голяма буква). Обикновена променлива може да се използва за константа, ако се загради в "\`\`": `` `localVariable` `` (в противен случай ще се счете за даване на име и ще въведе нова променлива).
    
    ```scala
    val perfectNumbers = List(6, 28, 496, 8128)
    
    list match
      case Nil => "Empty list"
      case `perfectNumbers` => "A list of perfect numbers"
      case List(math.Pi) => "A list of Pi"
      case List(42) => "A list of 42"
      case _ => "Some boring list"
    ```
* Съставни образци – деструктурират (разбиват) обект на части, като за всяка част също се посочва образец, който да се приложи на нея (който също може да бъде прост или съставен)
  * наредени n-торки – `(pattern1, pattern2, pattern3, pattern4)`
  
    ```scala
    (xs, ys) match
      case (Nil, Nil) => "Both lists are empty"
      case (Nil, y :: _) => s"The ys list has at least one element: $y"
      case _ => "The xs list is not empty"
    ```
    
    ```scala
    val (numer, denom) =
      val div = gcd(n, d)
      ((n / div) * d.sign, (d / div).abs)
    
    // We destructured the tuple into the variables number and denom
    ```
  * case class-ове – `Person(pattern1, pattern2, pattern3)`
    
    ```scala
    person match
      case Person("Boyan", age, location) => $"Boyan is $age years old and is from $location"
      case Person(name, UltimateAnswer, _) => s"A 42 years young person: $name"
      case Person(name, 34, _) => s"A 34 years young person: $name"
      case Person(name, _, _) => s"Someone: $name"
    ```
  * списъци – `head :: restOfTheList`
    
    ```scala
    elements match
      case first :: second :: rest =>
        s"The first element is $first, the second is $second and there are ${rest.size} more elements"
      case Nil => "The list is empty"
      case _ => "There is just one element"
    ```
  * колекции – `Seq(first, second, third)` или `Seq(first, second, rest*)`

    ```scala
    "2022-04-01-hello-there".split("-") match
      case Array(year, month, day) => s"Year: $year, month: $month, day: $day"
      case Array(year, month, day, rest*) =>
        s"Year: $year, month: $month, day: $day and some more elements: $rest"
      case _ => "Not enough elements"
    ```
  * **Пояснение:** всички съставни образци с фиксиран брой елементи са имплементирани чрез метод `unapply` на обекта/придружаващия обект, с който се match-ва, а тези с променлив брой елементи – чрез метод `unapplySeq`. Обектите с метод `unapply` или `unapplySeq` се наричат "екстрактори" и включват всички tuple-и, придружаващите обекти на case class-овете (за тях автоматично се генерира `unapply`), колекциите, деструктурирането на списъци чрез `::`, които описахме по-горе, но може също така да е и всеки произволен от нас обект. Вижте по-надолу секцията за екстрактори.
* Комбинация от име и съставен образец – `person @ Person(name, age, _)`. В ляво посочваме името, а в дясно образец за съпостяване:3434
  
  ```scala
  figure match
    case c @ Circle(radius) => s"Circle $c has radius $radius"
    case r @ Rectangle(a, b) => s"Rectangle $r has sides $a and $b"
  ```
  
  ```scala
  figures match
    case c @ Circle(radius) :: r @ Rectangle(a, b) :: rest =>
      s"First figure is a circle with radius $radius, second is a rectangle with sides $a and $b and there are ${rest.size} more figure"
  ```
* Алтернативи – `pattern1 | pattern2 | ...`
  
  ```scala
  boolean match
    case "true" | "True" | "TRUE" => true
    case "false" | "False" | "FALSE" => false
    case _ => false
  ```
  
* Guard-ове – `pattern if condition`

  ```scala
  person match
    case Person(name, age) if age < 30 => s"Young $name"
    case Person(name, _) => s"Old $name"
  ```

  ```scala
  val reciprocal: PartialFunction[Int,Double] =
    case x if x != 0 => 1.toDouble / x
  ```

## Къде можем да използваме pattern matching

* В `match` конструкции, използвайки `case` блок
  
  ```scala
  def toInteger(value: Int | String | Double): Int =
    value match
      case n: Int => n
      case s: String => s.toInt
      case d: Double => d.toInt
  ```
* В изрази на места, където се очаква стойност от тип `Function1[A, B]` или `PartialFunction[A, B]`

  ```scala
  Map(1 -> "one", 2 -> "two").map {
    case (number, letters) => s"$number: $letters"
  }
  // List("1: one", "2: two")
  ```
  
  ```scala
  List(Some(1), None, Some(42)).collect {
    case Some(n) => n * n
  }
  // List(1, 1764)
  ```
* При `val` дефиниции

  ```scala
  val person @ Person(name, age) = findPerson("id-123")
  val personDescription = s"$name, who is $age years old"
  ```
* При `for` в лявата част на неговите генератори и дефиниции

  ```scala
  val list1 = List(1, 2, 3, 4, 5)
  val list2 = List(10, 20, 30, 40, 50)
  val list3 = List(100, 200)
  
  for
    (a, b) <- list1 zip list2
    c <- list3
  yield (a + b + c)
  // List(111, 211, 122, 222, 133, 233, 144, 244, 155, 255)
  ```
  
  Pattern-а филтрира само елементите, които го match-ват:
  
  ```scala
  if n == 0 then List(Nil)
  else for
    first :: rest <- xs.tails.toList // filters out the empty list
    restCombination <- combinations(rest, n - 1)
  yield first :: restCombination
  ```
  
  Това изисква наличие на метод `filter`/`withFilter` на обекта от дясно. От Scala 3.2 филтриращото действие ще изисква думичката `case` пред pattern-а и това изискване ще бъде само в него случай. Повече информация [тук](https://docs.scala-lang.org/scala3/reference/changed-features/pattern-bindings.html).
* В try/catch блок за прихващане на изключения
  
  ```scala
  val parsedResult =
    try "42L".toInt
    catch
      case e: NumberFormatException => 0
  ```
  
  По-сложен пример би могъл да се структурира така:

  ```scala
  try doSomething()
  catch
    case e: Exception1 => alternative1
    // this catches more than one exception
    case _: Exception1 | _: Exception2 => alternative2
    // this is using a special extractor that ignores all exceptions that
    // generally shouldn't be caught, like VirtualMachineError, InterruptedException and others.
    // see https://scala-lang.org/api/3.x/scala/util/control/NonFatal$.html# for details
    case NonFatal(e) => alternative3
  ```

## Екстрактори

Екстракторите са обекти, които имат методи `unapply` или `unapplySeq` и позволяват деструктурирането на части на обекти от определен тип.

### Фиксиран брой елементи – чрез `unapply`:

```scala
object Email:
  def unapply(email: String): Option[(String, String)] = email.split("@", -1) match
    case Array(name, domain) => Some((name, domain))
    case _ => None

List("zdravko@gmail.com", "boyan@stemma.@", "viktor@uni-sofia.bg", "vas@sil@abv.bg", "yahoo.com")
  .collect {
    case Email(name, _) => s"$name's email"
  }
// List("zdravko's email", "viktor's email")
```

`unapply` метод бива генериран автоматично за всички case class-ове, благодарение на което всички те могат да бъдат използвани в pattern matching.

### Променлив брой елементи – чрез `unapplySeq`

```scala
object Words:
  def unapplySeq(str: String): Option[Seq[String]] =
    Some(str.split(" ").toSeq)

val phrase = "the quick brown fox"
val Words(first, second, _*) = phrase

s"$first-$second" // "the-quick"

"the quick brown fox" match
  case Words(first, second) => s"Exactly two words: $first, $second"
  case Words(_, _, rest*) => s"More than two words, the rest are: $rest"
```

```scala
import scala.util.matching.Regex
val ISODate = new Regex("""(\d{4})-(\d{2})-(\d{2})""")
val ISODate(year, month, day) = "2022-04-13"
```

### Любопитно: как работи `::`

Да разгледаме:

```scala
def quickSort(xs: List[Int]): List[Int] = xs match
  case Nil => Nil
  case x :: rest =>
    val (smaller, larger) = rest.partition(_ < x)
    quickSort(smaller) ::: (x :: quickSort(larger))
```

Знаем, че при `x :: quickSort(larger) `символът `::` е метод на `List`. При case `x :: rest` символът `::` изглежда като специален синтаксис от Scala. Но това не е така, `::`  всъщност е имлементиран в библиотеката на Scala и всеки би могъл да си добави подобна операция при pattern matching. Начинът, по който функционира, е следният. `List` в Scala e дефиниран по следния начин:

```scala
sealed trait List[+A]
case class ::[+A](head: A, next: List[A]) extends List[A]
case object Nil extends List[Nothing]
```

Забележете, че Cons класът се казва `::` , като той е case class и това значи, че има `unapply` метод. Това значи, че в Pattern Matching можем да пишем:

```scala
def quickSort(xs: List[Int]): List[Int] = xs match
  case Nil => Nil
  case ::(x, rest) =>
    val (smaller, larger) = rest.partition(_ < x)
    quickSort(smaller) ::: (x :: quickSort(larger))
```

Точно както при методите обаче, като всичко друго с двама участници, Scala ни позволява да напишем горното и инфиксно, с което получаваме познатия ни синтаксис: `x :: rest`.
