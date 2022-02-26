---
title: Въведение във функционалното програмиране със Scala
---

# Инсталиране на Scala

* Ще ни е нужна Java среда. Инсталирайте JDK (последната версия 17 работи перфектно, по-ранна също е ок)
* Не забравяйте да си зададете `JAVA_HOME` environment variable
* Инсталирайте [Coursier](https://get-coursier.io/docs/cli-installation)
* Пуснете `./cs setup`. Това ще ви инсталира няколко инструмента:
  - scala
  - scalac
  - sbt
  - scalafmt
  - amm
  - и други

# IDE или текстов редактор

Използвайте вашето любимо IDE или редактор:

* [IntelliJ Community Edition](https://www.jetbrains.com/idea/download/)
  - инсталирайте Scala plugin-а към него
* [Metals](https://scalameta.org/metals/) – имплементация на Language Server Protocol. Работи с:
  - Visual Studio Code
  - Vim
  - Emacs
  - Sublime Text
  - и други

# Scala инструменти

# Read-eval-print loop (REPL)

- интерактивен езиков шел
- стартира се от командния ред със `scala`

# Hello World

```scala
object HelloWorld:
  def main(args: Array[String]): Unit =
    println("Hello, World!")
```

# Компилиране и изпълнение

```
$ scalac HelloWorld.scala
$ scala HelloWorld
Hello, World!
```

# Hello World (Scala 3) { .scala3 }

```scala
@main
def hello = println("Hello, World!")
```

# Компилиране и изпълнение { .scala3 }

```
$ scalac HelloWorld.scala
$ scala hello
Hello, World!
```

# sbt, Scala/Simple Build Tool

build.sbt:

```scala
name := "hello-world"
version := "0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.11" % Test
)
```

# sbt -- Директорийна структура

- `build.sbt`
- `src/main/scala` -- основен код
- `src/test/scala` - тестове

# sbt команди

- sbt \<команда\> -- изпълнява командата
- sbt -- влиза в интерактивен режим
- compile -- компилира кода
- run -- изпълнява обект с `main` метод
- console -- стартира REPL, в който е достъпно всичко от кода
- test -- пуска всички тестове

# Фиксиране на sbt версия

`project/build.properties`:

```
sbt.version=1.6.2
```

# Тестове

Използваме библиотеката [ScalaTest](https://www.scalatest.org/)

```scala
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ExampleSpec extends AnyFlatSpec with Matchers:
  "+" should "sum two numbers" in {
    2 + 3 shouldBe 5
  }
```

# Включване на Java библиотеки

`build.sbt`:

```scala
name := "hello-world"
version := "0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  // Java библиотека, може да се използва директно в Scala код
  "com.google.guava" % "guava" % "30.1-jre",

  // Scala библиотека, %% залепва версията на Scala към името (т.е. akka-actor-typed_2.13)
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.13",

  // Test посочва, че библиотеката ще е налична само за тестовете (в src/test/scala)
  "org.scalatest" %% "scalatest" % "3.2.11" % Test
)
```

# Scala

# Типове и литерали

* `Boolean` (8 bits) – `true`, `false`
* `Char` (16 bits) – `'a'`, `'\n'`
* Числови типове
  * `Byte` (8 bits)
  * `Short` (16 bits)
  * `Int` (32 bits) – `42`, `0x2A`
  * `Long` (64 bits) - `100000L`, `0x186A0L`
  * `Float` (32 bits) – `3.14f`
  * `Double` (64 bits) – `3.14`
* `String` – `"Hey :)!"`

# Дефиниции

```scala
val a = 10 * 2 // неизменима променлива – винаги сочи към една и съща стойност
var b = 20 * 4 // изменима променлива – може да бъде пренасочвана
def c = 30 * 8 // стойността се преизчислява при всяко използване, не заема памет
```

<div class="fragment">

```scala
a = 100 // error: reassignment to val
b = 200 // успешно
```

</div>

# Type inference

```scala
// компилаторът сам открива типа на променливите – String и Int
val inferred = "Hello, hello, hello"
def inferredSize = inferred.size

// типът се задава явно
val explicit: String = "Is there anybody in there?" 
def explicitSize: Int = explicit.size
```

# Функции

```scala
def fortyTwo = 42
def sum(a: Int, b: Int) = a + b

sum(fortyTwo, 58) // 100
```

Посочването на типовете на параметрите е задължително

<div class="fragment">

```scala
// посочването на типа на връщания резултат е опционално,
// но препоръчително за публични функции
def twice(str: String): String = str * 2
twice(":)") // ":):)"
```

</div>

<div class="fragment">

```scala
println("You can see me on your screen :P") // Функциите могат да имат странични ефекти
```

</div>

# Къде живеят дефинициите?

* В блок
* Като членове на класове, обекти и др.
* (от Scala 3) на топ ниво на файл

# Блокове

```scala
def solveQuadraticEquation(a: Double, b: Double, c: Double) = {
  def squared(n: Double) = n * n
  
  val discriminant = squared(b) - 4 * a * c
  val discriminantSqrt = math.sqrt(discriminant)

  val firstSolution = (-b - discriminantSqrt) / (2 * a)
  val secondSolution = (-b + discriminantSqrt) / (2 * a)
   
  (firstSolution, secondSolution) // Наредена двойка. Повече за тях по-късно в курса :)
}
```

# Членове

```scala
object TestApp:
  val a = 10 * 2
  var b = 20 * 4
  def c = 30 * 8

  def sum = a + b + c

  @main
  def main(args: Array[String]) = println("The sum is: " + sum)
``` 

# Топ ниво във файл { .scala3 }

`TestApp.scala`:

```scala
val a = 10 * 2
var b = 20 * 4
def c = 30 * 8

def sum = a + b + c

@main def printSum = println("The sum is: " + sum)
```

# Файлове и пакети

Класовете, обектите, а от Scala 3 всички дефиниции,<br />
се поставят във файл със `.scala` разширение.

<div class="fragment">

Всеки от тях принадлежи на определен пакет,<br />който се отбелязва преди дефинициите:

```scala
package com.scalafmi

val a = 10 * 2
var b = 20 * 4
def c = 30 * 8

def sum = a + b + c

@main def printSum = println("The sum is: " + sum)
```
</div>

<p class="fragment">
Конвенция е файловете да се намират в директория,<br />съответстваща на пакета им.
</p>

# Още дефиниции

```scala
type Person = String
type Address = String

def describe(name: Person, address: Address) = name + "'s address is: " + address

describe("Viktor", "Pazardzhik") // "Viktor's address is: Pazardzhik"
```

<div class="fragment">

```scala
val ThisIsAConstantByConvention = 3.14 // имената на константите започват с главна буква
```

</div>

# Още низови литерали

* Интерполация
* raw низове
* Многоредови низове

# Интерполация

```scala
def describe(name: Person, address: Address, age: Int) =
  s"$name's address is: ${twice(address)}. He is $age years old"

// "Viktor's address is: PazardzhikPazardzhik. He is 25 years old"
describe("Viktor", "Pazardzhik", 25)
```

# raw низове

```scala
val evilLaughter = "Muhahahaha :D!"
raw"Escaping has no\\power \t here\n. $evilLaughter" // Escaping has no\\power \t here\n. Muhahahaha :D!
"Escaping works \\ \there\n" // Escaping works \     here<newline>
```

Полезно за регулярни изрази и други

# Многоредови низове

```scala
val sql = """
    SELECT id, name, address, age
    FROM Person
    WHERE hometown = 'Varna'
    """
```

<div class="fragment">

Премахване на интервалите в началото:

```scala
 val sql =
   """SELECT id, name, address, age
      |FROM Person
      |WHERE hometown = 'Varna'
      |""".stripMargin
```

</div>

# Всяка стойност е обект

<div class="fragment">

Включително тези на основните типове

```scala
-42.abs // 42
1.to(10) // Range 1 to 10 (inclusive)
1.until(10) // Range 1 until 10
1.+(2) // 3, всички оператори са също методи!
4.*(5) // 20
```

</div>

<div class="fragment">

Инфиксен запис (само за методи на един параметър)

```scala
1 to 10 // Range 1 to 10 (inclusive)
1 until 10 // Range 1 until 10
1 + 2 // 3
4 * 5 // 20
```

</div>

# Да, имената могат да са символни:

```scala
val ==> = "An arrow"
def **(a: Double, b: Double): Double = math.pow(a, b)

**(2.0, 10.0) // 1024.0
```

<div class="fragment">

Не могат да се смесват с букви/цифри:

```scala
val **Power = "**" // грешка!
```

</div>

# `if` конструкция

```scala
val n = 72

if n > 42 then "Greater than the ultimate answer"
else if n == 42 then "The answer"
else "Not there yet"
```

# Всичко е израз

<p class="fragment">И си има стойност и тип</p>

<div class="fragment">

```scala
val n = 72

val evaluation =
  if n > 42 then "Greater than the ultimate answer"
  else if n == 42 then "The answer"
  else "Not there yet"

evaluation // Greater than the ultimate answer
```

</div>

<div class="fragment">

```scala
def fact(n: Int): Int =
  if n <= 1 then 1
  else n * fact(n - 1)
```

</div>

# В Java (и други)

```java
public int maxSquared(int a, int b) {
  int max;
  
  if (a > b) {
    max = a;
  } else {
    max = b;
  }

  return max * max;
}
```

В Java сме задължени да извършим мутация/страничен ефект в `if`

# В Scala

```scala
def maxSquared(a: Int, b: Int) = {
  val max = if a > b then a else b
  max * max
}
```

В Scala изглежда по-математично и ясно

# Блоковете също са изрази!

```scala
val a = 2
val b = 10
val c = 5

val discriminantSqrt = {
  val discriminant = b * b - 4 * a * c

  math.sqrt(discriminant)
}
```

```scala
def maxSquared(a: Int, b: Int) = {
  val max = if a > b then a else b
  max * max
}
```

Оценяват се до последния statement в тях

# Блоковете като изрази (допълнение)

```scala
def squared(n: Int) = n * n

squared(10)
squared({
  val complicatedCalculation = 1 + 2 + 3 + 4

  complicatedCalculation / 2 
})
```

<div class="fragment">

```scala
squared {
  val complicatedCalculation = 1 + 2 + 3 + 4

  complicatedCalculation / 2 
}

println {
   val result = squared(42)

   s"The result is: $result"
}
```

При функции на един параметър Scala ни позволява<br />да пропускаме кръглите скоби

</div>

# Блокове чрез индентация { .scala3 }

Scala 3 ни позволява вместо чрез скоби да определяме блокове чрез индентация:

::: { .fragment }

```scala
def solveQuadraticEquation(a: Double, b: Double, c: Double) =
  def squared(n: Double) = n * n
  
  val discriminant = squared(b) - 4 * a * c
  val discriminantSqrt = math.sqrt(discriminant)

  val firstSolution = (-b - discriminantSqrt) / (2 * a)
  val secondSolution = (-b + discriminantSqrt) / (2 * a)
   
  (firstSolution, secondSolution) // Наредена двойка. Повече за тях по-късно в курса :)
```

:::

::: { .fragment }

```scala
val theAnswerSquared =
  val a = 42
  a * a
```

:::

# Блокове чрез индентация { .scala3 }

```scala
if n > 42 then 
  val greater = "Greater"
  s"$greater than the ultimate answer"
else if n == 42 then "The answer"
else "Not there yet"
```

::: { .fragment }

От тук нататък ще използваме само този стил

:::

::: { .fragment }

При аргументи на функции все пак сме задължени да използваме скобите:

```scala
println {
  val result = squared(42)
  
  s"The result is: $result"
}
```

:::

# Въпроси :)?
