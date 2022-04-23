# Вариантност (Variance)

Вариантността определя как съвместимостта (subtyping-а) на параметризиран (generic) тип се отнася към съвместимостта (subtyping–а) на неговите типови параметри. Най-лесно това се вижда чрез пример.

Нека имаме йеархия за плодове:

```scala
trait Fruit:
  def color: String
  def size: Int

case class Apple(color: String, size: Int) extends Fruit
case class Orange(sort: String, size: Int) extends Fruit:
  def color: String = "orange"
```

`List[Apple]` е подтип на (съвместим със) `Seq[Apple]` при всички случаи. Въпросът е дали `List[Apple]` е подтип на `List[Fruit]`? Или за който и да е параметризиран тип `GenericType[Apple]` дали е подтип на `GenericType[Fruit]`?

Отговорът е, че това зависи от вариантността на параметризирания тип – дали той е инвариантен, ковариантен или контравариантен. Да разгледаме трите случая.

## Инвариантност

По подразбиране всички параметризирани (generic) типове са инварианти. Отговор на въпроса за съвместимостта най-лесно можем да получим, ако разгледаме един изменим списък. Изменимият списък като тип е инвариантен. Да видим следния пример:

```scala
// importing the mutable version of List
import scala.collection.mutable.ListBuffer

val apples: ListBuffer[Apple] = ListBuffer(Apple("red", 2), Apple("green", 5), Apple("yellow", 3))
val fruits: ListBuffer[Fruit] = apples
```

Ако горното се компилира, `fruits` би имало всички операции за изменим списък:

```scala
fruits += Orange("navel", 8)
```

Така добавихме портокал към списъка от плодове, но тъй като `fruits` и `apples` сочат към един и същи обект (тъй като са референции), това означава, че добавихме портокал към списъка от ябълки, което е напълно неочаквано и счупва гаранциите, които имаме от типа `ListBuffer[Apple]`. Поради тази причина Scala всъщност ни забранява да присвоим `ListBuffer[Apple]` на променлива от тип `ListBuffer[Fruit]`.

Така инвариантните типове са съвместими само и единствено когато типовете им параметри съвпадат напълно, тоест `ListBuffer[Apple]` е съвместимо само с `ListBuffer[Apple]` и с `scala.collection.mutable.Seq[Apple]`, но не и с `ListBuffer[Fruit]` или `scala.collection.mutable.Seq[Fruit]`.

Инвариатността не поставя никакви ограничения върху типовете в сигнатурата на методите на параметризирания тип. За да получим съвместиммост при съвместими (а не само при съвпадащи) типови параметри, то е необходимо да поставим ограничения върху това с какви типове можем да дефинираме неговите методи. Scala поддържа това чрез ковариатност и констравариантност.

## Ковариантност

Ковариантността и контравариатността се определят поотделно за всеки типов параметър на параметризирания тип. За да бъде типовия параметър ковариантен е необходимо да запишем `+` пред него:

```scala
sealed trait List[+A]
case class Cons[+A](head: A, tail: List[A]) extends List[A]
case object Nil extends List[Nothing]
```

Ковариантността поставя ограничение на методите, използващи ковариантния типов параметър в своята сигнатура. Те могат без проблеми да го връщат като тип, но им е забранено да го приемат в своите параметри. Така ковариантните типови параметри всъщност се използват за дефиниране параметризирани типове, които произвеждат стойности от съотетния типов параметър. Така вече:

```scala
val apples: List[Apple] = List(Apple("red", 2), Apple("green", 5), Apple("yellow", 3))
val fruits: List[Fruit] = apples
```

Неизменимия списък `List` е ковариантен и горният код се компилира успешно. Списъкът има операция `prepended` (още известна като `::`):

```scala
val moreFruits: List[Fruit] = fruits.prepended(Orange("navel", 8))
```

На нея обаче ѝ е забранено да приема само и единствено типовия си параметър `A`. Нека да видим нейната сигнатура:

```scala
sealed trait List[+A]:
  def prepended[AA >: A](elem: AA): List[AA] = elem :: this
```

Поради ковариантността на `elem` е забранено да бъде от тип `A` (например конкретно от тип `Apple`). Когато обаче дефинираме функцията `prepended` да приема който и да е надтип на `A` това ограничение отпада и горният код се компилира. Защо това е така? Ако се замислим внимателно проблемът при примера от инвариантния списък беше, че методът на списъкът очакваше само и единствено типът `A`, тоест типът `Apple`, и той не би могъл да работи с който и да е друг тип, който не е съвместим с `Apple` – за него това  би било неочаквано. При ковариантност обаче задължаваме списъка, ако желае да приеме типът `A` (например `Apple`) то да очаква, че ще може да поеме и който и да е надтип на `A` (например `Fruit`). Така значително ограничаваме методът и откъм възможна имеплементация, тъй като вече трябва да работи и с по-общ тип. Случая би било много трудно (почти невъзможно) да напишем смислена имплементация на `prepended`, която да връща `List[A]`, затова и връщаме `List[AA]`.

Всичко това означава и че `prepended` може да поеме по-общия тип и дори докато работим със списъка от ябълки:

```scala
val apples: List[Apple] = List(Apple("red", 2), Apple("green", 5), Apple("yellow", 3))
val fruits = apples.prepended(Orange(8))

// The type of fruits will be automatically infered as List[Fruit]
```

Всичко това е благодарение на това, че имаме смислена имплементация на `prepended` за неизменимия списък. За изменим списък трудно бихме имплементирали операция `prepend`, приемаща по-общ тип, по смислен начин. Затова и много често ковариантността работи при неизменими структури.

В обобщение, ако имаме параметризиран тип с ковариантен параметър `GenericType[+T]`, то `GenericType[B]` е подтип на `GenericType[A]` тогава и само тогава, когато `B` е подтип на `A`. Или казано с плодове: `GenericType[Apple]` е подтип на (съвместим с) `GenericType[Fruit]`, тъй като `Apple` е подтип на `Fruit`.

## Контравариантност

За да бъде типов параметър контравариантен е необходимо да се запише `-` пред него:

```scala
trait Consumer[-A]:
  def consume(a: A): String
```

Контравариантността позволява на методите на параметризирания тип без проблем да приемат контравариантния типов параметър, но им забранява да го връщат като резултат. Така ковариантните типови параметри се използват за дефиниране параметризирани типове, които консумират стойности от съотетния типов параметър (вероятно произвеждайки друг тип).

Като пример, нека да имаме консуматор на плодове и консуматор на портокали:

```scala
object FruitConsumer extends Consumer[Fruit]:
  def consume(fruit: Fruit): String = 
    s"Nom, nom, nom, I love all kinds of fruits. This ${fruit.color} fruit is delicious!!!"

object OrangeConsumer extends Consumer[Orange]:
  def consume(orange: Orange): String =
    s"Nom, nom, nom, I absolutely love oranges. This ${orange.sort} sort of oranges is so tasty!!!"
```

със следната функция:

```scala
def dineFruits(fruits: List[Fruit])(fruitLover: Consumer[Fruit]): List[String] =
  fruits.map(fruitLover.consume)
```

Тогава на `dineFruits` можем съвсем спокойно да подадем `FruitConsumer`, но не и `OrangeConsumer` (въпреки че `Orange` е подтип на `Fruit`):

```scala
val fruits: List[Fruit] = List(Orange("navel", 3), Apple("red", 10))

dineFruits(fruits)(FruitConsumer)

// List(
//   Nom, nom, nom, I love all kinds of fruits. This orange fruit is delicious!!!,
//   Nom, nom, nom, I love all kinds of fruits. This red fruit is delicious!!!
// )

// Does not compile:
// dineFruits(fruits)(OrangeConsumer)
```

Поглеждайки и имплементацията на `OrangeConsumer` това е логично – той няма как да вземе сорта на произволен плод, тъй като не всеки плод има сорт като поле.

Обратно, нека имаме:

```scala
def dineOranges(oranges: List[Orange])(orangeLover: Consumer[Orange]): List[String] =
  oranges.map(orangeLover.consume)
```

Тук като консуматор можем да използваме както консуматорът на портокали, така и консуматорът на плодове:

```scala
val oranges: List[Orange] = List(Orange("navel", 6), Orange("bergamot", 2))

dineOranges(oranges)(FruitConsumer)
// List(
//   Nom, nom, nom, I love all kinds of fruits. This orange fruit is delicious!!!,
//   Nom, nom, nom, I love all kinds of fruits. This orange fruit is delicious!!!
// )

dineOranges(oranges)(OrangeConsumer)
// List(
//   Nom, nom, nom, I absolutely love oranges. This navel sort of oranges is so tasty!!!,
//   Nom, nom, nom, I absolutely love oranges. This bergamot sort of oranges is so tasty!!!
// )
```

Така `Consumer[Fruit]` е подтип (е съвместим със) `Consumer[Orange]`, тъй като `Fruit` е надтип на `Orange`.

Контравариантността много често се използва освен при консумиращи типове, така и при encoder-и, произвеждащи конкретен тип.

В обобщение, ако имаме параметризиран тип с контравариантен параметър `GenericType[-T]`, то `GenericType[A]` е подтип на `GenericType[B]` тогава и само тогава, когато `A` е надтип на `B`. Или казано с плодове: `GenericType[Fruit]` е подтип на (съвместим с) `GenericType[Orange]`, тъй като `Fruit` е надтип на `Orange`.

## Вариантност на функции

Обектните функции в Scala са типичен пример за параметризиран тип, който смесва и контравариантност (за входните си параметри) и ковариантност (за резултатния параметър) в своята дефиниция:

```scala
trait Function2[-T1, -T2, +R]:
  def apply(v1: T1, v2: T2): R
```

Ако имаме следния тип:

```scala
trait Meal
case class Salad(description: String) extends Meal
case class Soup(description: String) extends Meal
```

И следния метод:

```scala
def prepareOranges(cook: Orange => Meal): List[Meal] = oranges.map(cook)
```

То бихме могли да го извикаме така:

```scala
val fruitSaladCook: Fruit => Salad = fruit => 
  Salad(s"Fruit salad with ${fruit.color} color")

prepareOranges(fruitSaladCook)

// Fruit is a supertype of Orange
// Salad is a subtype of Meal
```

Това удобство е нещо, което силно лиспва в Java например, където вариантността не се специфицира при декларация на тип, ами вместо това е необходимо да се посочи при всеки метод, използващ типа, чрез type bounds. Може да разгледате например как в стандартната библиотека на Java при много от методите, приемащи ламбда функции, се посочва type bound (`extends` или `super` в термините на Java) за всеки от типовите параметри на функцията.
