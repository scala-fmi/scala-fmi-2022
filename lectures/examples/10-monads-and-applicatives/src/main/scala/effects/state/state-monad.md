# State монада

Целия пример може да видите в кода от лекциите в [`RNG.scala`](RNG.scala).

Като пример ще разгледаме генератор на псевдослучайни числа, като типично нещо, обикновено имплементирано чрез mutable state. Стандартният начин на използване е:

```scala
val random = new Random
  
val a = random.nextInt
val b = random.nextInt
val c = random.nextBoolean
```

Като тук всяко извикване на `nextInt` и `nextBoolean` променя състоянието на `random`, с което се губи и референтната прозрачност.

Типично функционално решение на този проблем би изглеждало така:

```scala
val rng1 = RNG(initialSeed)

val (rng2, a) = rng1.nextInt
val (rng3, b) = rng2.nextInt
val (_, c) = rng3.nextBoolean
```

Тоест `nextInt` и `nextBoolean` връщат не само следващата случайна стойност, но и следващото състояние на генератора на случайни числа. За разлика от първия пример, те по никакъв начин не променят инстанцията, сочена от `rng1`. Така получаваме решение без mutability.

`RNG.nextInt` е със сигнатура `def nextInt: (RNG, Int)` ([код](RNG.scala), имплементацията е от червената книга за Scala)

Проблемът на решението е, че имаме досадно влачене на състояние, което е податливо на грешки (трябва да внимаваме да реферираме правилната версия на генератора).

Тази ситуация бихме имали при всеки възможен вид състояние, та да пробваме да го изнесем в ефект. Функциолно генерирането на стойност и ново състояние от текущо състояние изразяваме чрез функция `S => (S, A)`, където `S` е типа за състояние, а `A` на генерираната стойност. Нека да сложим тази функция в тип, който ще направим ефектен:

```scala
case class State[S, A](run: S => (S, A))
```

Да имплементираме монадна инстанция за този тип, която да ни позволи да композираме генерирането на стойности, всяка от съответното ѝ състояние. За примера горе с генератора на случайни числа това значи всяко последвашо случайно число да бъде генерирано от правилното последващо състояние на генератора. Инстанцията ще бъде следната:

```scala
object State:
  given [S]: Monad[[A] =>> State[S, A]] with
    extension [A](fa: State[S, A])
      def flatMap[B](f: A => State[S, B]): State[S, B] = State { s1 =>
        val (s2, a) = fa.run(s1)
        f(a).run(s2)
      }

    def unit[A](a: A): State[S, A] = State(s => (s, a))
```

Разглеждаме `State` като ефект по втория си параметър, а за всеки тип `S` той ще има различна монадна инстанция. Синтаксисът `[A] =>> State[S, A]` дефинира ламбда функция на ниво типове, която приема типов параметър `A` и генерира специфичен тип `State[S, A]`. Повече за този синтаксис може да видите в [State.scala](State.scala) и .

Забележете, че кодът на `flatMap` много прилича на следните два реда, които бяха по-горе (върнатият резултат също е двойка):

```scala
val (rng2, a) = rng1.nextInt
val (rng3, b) = rng2.nextInt
```

Имплементацията на `unit` винаги генерира `a`, независимо от това какво е състоянието `s`, а самото `s` остава непроменено. Тоест това е `State` инстанция, която винаги генерира константа.

Да се върнем към примера със случайните числа и да използваме `State` за него. Можем да създадем `State`, генериращ случаен `Int`, по следния начин:

```scala
val nextInt: State[RNG, Int] = State((rng: RNG) => rng.nextInt)
```

Той приема текущо състояние на генератора `RNG` и връща двойка от новото състояние на генератора и генерираното число.

По лесен начин можем да трансфомираме този `State` обект към нов, който да генерира `Boolean` стойности:

```scala
val nextBoolean: State[RNG, Boolean] = nextInt.map(_ >= 0)
```

Тук използваме операцията `map`, която получаваме благодарение на това, че предоставихме монадна инстанция на `State`. `map` получава генерираното случайно число и го преобразува в `true` или `false` в зависимост от това дали е неотрицателно или не. Забележете, че `map` не се интересува от състоянието на генератора, то се обновява автоматично чрез монадата (`flatMap` се грижи за това вътрешно).

Сега вече можем да направим следното:

```scala
val randomTuple = for
  a <- nextInt
  b <- nextInt
  c <- nextBoolean
yield (a, b, a + b, c)
```

Тук отново имаме последователни извиквания на `nextInt` (два пъти) и `nextBoolean`, но за разлика от първоначалния чисто функционален код, вече не се налага да предаваме състоянието на генератора ръчно – монадната инстанция на `State` прави това автоматично за нас.

При горния код все още не се изпълнява нищо. Може да стартираме изчислението по следния начин:

```scala
val intialRng = RNG(System.currentTimeMillis) // use current time as random seed
val (_, result) = randomTuple.run(intialRng)
```

Така `result` съдържа генерираната случайна четворка.
