---
title: Type Classes
---

# Докъде сме?

::: { .fragment }

[Аспекти на ФП](02-fp-with-scala.html#/как-да-пишем-функционално)

:::

# Абстрактност

# Абстрактността в математиката

<p class="fragment">Примери: групи, полета, полиноми, векторни пространства и много други</p>

<p class="fragment">Алгебрични структури – множества със съответни операции и аксиоми (свойства)</p>

<p class="fragment">алгебрични структури ~ тип данни</p>

# Група

::: { .fragment }

Нека G е множество с бинарна операция „·“

G наричаме група, ако:

:::

::: incremental

* асоциативност – ∀ a, b, c ∈ G:
  
  ```
  (a · b) · c = a · (b · c)
  ```
* неутрален елемент – ∃ e ∈ G, такъв че ∀ a ∈ G
  
  ```
  e · a = a · e = a
  ```
* обратен елемент – ∀ a ∈ G, ∃ a' ∈ G, такъв че
  
  ```
  a · a' = a' · a = e
  ```

:::

# Моноид

Нека M е множество с бинарна операция „·“

M наричаме моноид, ако:

* асоциативност – ∀ a, b, c ∈ M:
  
  ```
  (a · b) · c = a · (b · c)
  ```
* неутрален елемент – ∃ e ∈ M, такъв че ∀ a ∈ M
  
  ```
  e · a = a · e = a
  ```

# Реализация?

::: { .fragment }

Задача: напишете метод `sum` работещ със списъци от различни типове

```scala
sum(List(1, 3, 4))
sum(List("a", "b", "c"))
sum(List(Rational(1, 2), Rational(3, 4)))
```

:::

# Контекст в програмния код

<p class="fragment">в математиката: „Нека фиксираме поле F, такова че...“</p>
<p class="fragment">в математиката: „Нека фиксираме ортогонална координатна система“</p>

#

<dl>
    <dt>context</dt>
    <dd>1. The parts of a written or spoken statement that precede or follow a specific word or passage, usually influencing its meaning or effect;</dd>
    <dd class="fragment">2. The set of circumstances or facts that surround a particular event, statement, idea, etc.</dd>
    <dd class="fragment">3. “What comes with the text, but is not in the text.”</dd>
</dl>

# Примери

Текуща:

* конфигурация
* транзакция
* сесия
* ExecutionContext – pool от нишки
* ...

# Контекст в програмния код

* import
* подтипов полиморфизъм
* dependency injection
* външен scope
* параметри

# Експлицитно предаване на контекст

# Имплицитно предаване на контекст

::: { .fragment }

В математиката: „Дадено е поле F, такова че...“

:::

::: { .fragment }

В Scala 3:

```scala
given f: Field[Double] = ???
```

:::

# Context bound

```scala
def sum[A : Monoid](xs: List[A])
```

# Логическо програмиране<br />в типовата система

::: incremental

* Типовата система е напълно логическа
* Търсенето на given стойности от определен тип съвпада с механиката на логическите изводи, позната ни от логическото програмиране
* При изводите *given без параметри* съответства на логически факти, а *given с параметри* на логически правила 

:::

#

Type class-овете дефинират операции и аксиоми/свойства, които даден тип трябва да притежава.

<p class="fragment">За да бъде един тип от даден клас, то трябва да предоставим валидна имплементация на операциите на type class-а</p>

# Аксиомите са важни

((a · b) · c) · d – едно по едно, от ляво надясно

(a · b) · (c · d) – балансирано и паралелизуемо

<p class="fragment">Могат да бъдат проверявани чрез тестове</p>

# `fold` vs `foldLeft`

```scala
(1 to 100000000).par.fold(0)(_ + _)
```

<p class="fragment">`fold` изисква асоциативна операция</p>

# Контекст в Scala 2

# ООП класове срещу type class-ове

<p class="fragment">Класовете в ООП моделират обекти</p>

<p class="fragment">Type class-овете моделират типове</p>

# Полиморфизъм

<p class="fragment">Използването на един и същи интерфейс с различни типове</p>

# Полиморфизъм в Scala

# Параметричен полиморфизъм (generics)

```scala
def mapTwice[A](xs: List[A])(f: A => A): List[A] =
  xs.map(f compose f)

mapTwice(List(1, 2, 3))(_ * 2) // List(4, 8, 12)
mapTwice(List("ab", "c", "def"))(str => str + str) // List(abababab, cccc, defdefdefdef)
```

# Ad hoc полиморфизъм

Избор на конкретна имплементация според конкретния тип

# Ad hoc полиморфизъм – overloading

```scala
def stringify(n: Int) = n.toString
def stringify(n: Rational) = s"$n.numer/$n.denom"

stringify(1) // "1"
stringify(Rational(1)) // "1/1"
```

# Ad hoc полиморфизъм – type classes

Пример: реализацията на <code>Monoid</code> се избира конкретно според типа

```scala
sum(List(Rational(2), Rational(4))) // rationalMonoid
sum(List(2, 4)) // intMonoid
```

# Подтипов полиморфизъм

```scala
trait Figure:
  def area: Double
  def circumference: Double

case class Circle(radius: Double) extends Figure:
  def area: Double = Pi * radius * radius
  def circumference: Double = 2 * Pi * radius

case class Square(side: Double) extends Figure:
  def area: Double = side * side
  def circumference: Double = 4 * side

val figure = getRandomFigure(10)
figure.area // 100
```

<p class="fragment">Липсва информация за конкретния тип, но се изпълнява конкретна имплементация</p>

# Duck typing и структурно подтипизиране

```scala
type Closable = { def close(): Unit }

def handle[A <: Closable, B](resource: A)(f: A => B): B =
  try f(resource) finally resource.close()

handle(new FileReader("file.txt"))(f => readLines(f))
```

# Binding

::: { .fragment }

* Static (compile time) – параметричен и ad-hoc полиморфизъм
* Late (runtime) – подтипов полиморфизъм и duck typing/структурно типизиране

:::

::: { .fragment }

Late binding-а е фундаментален за ООП

:::

# Ретроактивност

разширяване на тип без промяна на кода му

# Ретроактивен полиморфизъм

::: { .fragment }

добавяне на интерфейс към тип<br />без промяна на кода му

:::

::: { .fragment }

Type class-овете поддържат ретроактивен полиморфизъм

:::

# Numeric

# Ordering

# Сериализация до JSON

::: { .fragment }

По-късно в курса ще разгледаме библиотеката [`Circe`](https://circe.github.io/circe/)

:::

# Езици, поддържащи type class-ове

* Haskell
* Scala
* Rust
* Idris
* ...

#

В Haskell всеки type class може да има<br />само една инстанция за определен тип.

::: { .fragment }

В Scala липсва такова ограничение, което е едновременно и плюс и минус.

:::

# Библиотеки за type class-ове?

![](images/09-type-classes/cats-cat.png){ height="520" }

# Библиотеки

::: incremental

* Общи
  * [![](images/09-type-classes/cats-small.png){ height="40" style="vertical-align: middle" } Cats](http://typelevel.org/cats/)
  * [Scalaz](https://scalaz.github.io)
* В конкретен домейн
  * [Spire](https://typelevel.org/spire/) – математически абстракции, използва Cats
  * [Cats Effects](https://typelevel.org/cats-effect/) – абстракции за асинхронност
  * ...

:::

# Категории

[![](images/09-type-classes/category-theory-for-programmers.png){ height="520" }](https://github.com/hmemcpy/milewski-ctfp-pdf)

::: { .fragment }

[лекции в Youtube](https://www.youtube.com/watch?v=I8LbkfSSR58&list=PLbgaMIhjbmEnaH_LTkxLI7FMa2HsnawM_)

:::

# Cats

::: { .fragment }

Различни видове котк... категории 😸

:::

::: { .fragment }

![Vivian – Scala Magician](images/09-type-classes/vivian-boyan-cat.jpg){ height=480 }

:::

# Моноид в Cats

# Multiversal equality в Cats (`Eq`)

# Scala with Cats

[![Scala with Cats](images/09-type-classes/scala-with-cats.png){ height="520" }](https://underscore.io/books/scala-with-cats/)

# В заключение

Type class-овете:

:::incremental

* моделират типове
* предоставят общ интерфейс и аксиоми за цяло множество от типове
* или още – общ език, чрез който да мислим и боравим с тези типове
* позволяват ad hoc полиморфизъм
* наблягат на композитността и декларативността
* добавят се ретроактивно към типовете и в Scala могат да бъдат контекстно-зависими

:::

# Въпроси :)?
ч