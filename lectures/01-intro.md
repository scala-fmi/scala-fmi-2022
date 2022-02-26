---
title: За курса
---

# Функционално програмиране за напреднали със Scala

2021/2022

![](images/scala-logo.png)

# Днес :)

![](images/01-intro/pretending-to-write.gif){ height=180 }

::: incremental

* Кои сме ние?
* Административни неща за курса
* Защо функционално? Кратка история на функционалното
* Защо Scala?
* Kакво ще учим?
* Ресурси
* Инсталиране и инструменти
* Начало на първа тема: Въведение във ФП със Scala

:::

# Ние?


<ul style="display: grid; grid-template-columns: repeat(2, 1fr); list-style: none">
<li>[![](images/01-intro/boyan.jpg){ height=100 style="vertical-align: middle; border-radius: 50%" } Боян Бонев](https://www.linkedin.com/in/boyan-bonev-ams/)</li>
<li>[![](images/01-intro/zdravko.jpg){ height=100 style="vertical-align: middle; border-radius: 50%" } Здравко Стойчев](https://www.linkedin.com/in/zdravko-stoychev-46414758/)</li>
<li>[![](images/01-intro/viktor.jpg){ height=100 style="vertical-align: middle; border-radius: 50%" } Виктор Маринов](https://www.linkedin.com/in/viktor-marinov-07374512b/)</li>
<li>[![](images/01-intro/vassil.jpg){ height=100 style="vertical-align: middle; border-radius: 50%" } Васил Дичев](https://www.linkedin.com/in/vdichev/)</li>
<li>[![](images/01-intro/dany.jpg){ height=100 style="vertical-align: middle; border-radius: 50%" } Дани Янев](https://www.linkedin.com/in/dany-yanev/)</li>
</ul>

# Сбирки и админастративни неща

::: incremental

* Всяка сряда от 18:15 до 21:00
* Онлайн (поне засега)
* Платформа? Или Google Meet или Microsoft Teams (ще я фиксираме тези дни)
* Slack – най-лесно може да ни намерите там. Ще постваме всичко ново там
* Материали в GitHub: [https://github.com/scala-fmi/scala-fmi-2022](https://github.com/scala-fmi/scala-fmi-2022)
* Домашни и финален проект – GitHub Classroom

:::

# Оценяване?

::: { .fragment }

* Домашни през семестъра: 50 точки
* Два теста: 50 точки
* Финален проект: 50 точки
* Бонус точки
* Общо: 150+

:::

::: { .fragment }

**Скала** за оценяване (pun intended):

+--------+----------+-------------+
| Оценка | Точки    | Дял         |
+--------+----------+-------------+
| 6      | ≥ 120    | 80%         |
+--------+----------+-------------+
| 5      | 103--119 | 69%         |
+--------+----------+-------------+
| 4      | 86--102  | 57%         |
+--------+----------+-------------+
| 3      | 68--85   | 45%         |
+--------+----------+-------------+
| 2      | \< 68    | < 45%       |
+--------+----------+-------------+

:::

# Представете ни се за бонус точки

::: { .fragment }

Споделете няколко думи за себе си и своите интереси и качете снимка с инсталирана и работеща Scala в [**#да-се-представим**](https://scalafmi.slack.com/archives/C01P1KBSF54) в Slack за **2 бонус точки**.

:::

# Функционално? Кратка история

* 1930–1940 – Ламбда смятане от Алонсо Чърч
  - модел на изчисление, базиран на композиция на анонимни функции
* 1958 – LISP от Джон МакКартни – първи език за ФП
  - Повлиян от математиката
  - Рекурсия като цел
  - Въвежда garbage collection
  - Кодът е данни
* 1973 – ML от Робин Милнър
  - статично типизиране
  - type inference. Типова система на Хиндли-Милнър
  - Параметричен полиморфизъм (Generics)
  - Pattern matching
* 1980-те – допълнително развитие, Standard ML, Miranda (lazy evaluation)

# Функционално? Кратка история

* 1986 – Erlang от Джо Армстронг и Ериксън
  - фокус на телекоми и толерантни на грешки дистрибутирани системи
* 1990 – Haskell, отворен стандард за ФП език
  - абстракция чрез type classes
  - контролиран монаден вход/изход
* 2000-те – Scala от Мартин Одерски (2004-та), Clojure от Рич Хики (2007-ма)
  - Неизменими структури от данни
  - Средства за конкурентни и дистрибутирани системи
  - Комбинация на практики от различни езици (напр. ООП + ФП в Scala)
* Края на 2000-те до наши дни – ФП елементи се появяват в почти всеки език. Защо?

# Мартин Одерски


<div style="width: 45%; float: left"><img src="images/01-intro/odersky.JPG" /></div>

<div style="width: 50%; float: right">

* Професор в EPFL, Лозана
* Възхитен от ФП във време, в което Java изплува като платформа
* Решава да ги обедини в нов език – Pizza 🍕 – като добави елементи към Java
    * Параметричен полиформизъм (Generics)
    * Функции от по-висок ред
    * Pattern matching
* Имплементацията му на generics и изцяло новият компилатор, който написва, стават част от Java

</div>

# Стъпка назад

* Java има много ограничения
* Как би изглеждал език, комбиниращ ФП и ООП, ако го дизайнваме в момента?

# SCAlable LAnguage (Scala) – Януари 2004-та

::: incremental

* Без (твърде) много feature-и в самия език
* Вместо това малко, но пълно множество от мощни езикови конструкции
* Имплементиране на feature-и в библиотеки, използвайки тези конструкции

:::

::: { .fragment }

Език, който скалира според нуждите

:::

# [Малка граматика](https://docs.scala-lang.org/scala3/reference/syntax.html)

![](./images/01-intro/grammar-size.png){ height="480" }

::: { .fragment }

Малко, но мощни езикови средства,<br />които се комбинират добре едно с друго

:::

# Детайлна типова система

![](./images/01-intro/static-type-system.jpeg){ height="560" }

#

Симбиоза на ФП и ООП

# Scala 3

* Чакахме го 8 години :)
* DOT – математически модел за [есенцията на Scala](https://www.scala-lang.org/blog/2016/02/03/essence-of-scala.html) на ниво типова система
* Dotty – изцяло нов компилатор с модерен дизайн, базиран на DOT
* Опростяване, изчистване и допълване на езика ([списък на какво ново](https://docs.scala-lang.org/scala3/new-in-scala3.html))

# Scala 3

::: incremental

* Курсът ни ще се базира на Scala 3
  
  ![](images/01-intro/cheering-minions.gif)
* Ще отбелязваме кои са новите feature-и
* Ще ви срещнем и с мъничко Scala 2 синтаксис
  - много код все още използва него

:::

# Scala по света

::: incremental

* Може да се срещне в production общо-взето навсякъде по света
* Особено при дистрибутирани системи или ML/Data Science
* По света: Twitter, Netflix, Disney+, LinkedIn, Databricks, The Guardian, Airbnb и много други
* В България: В Ocado Technology, AtScale, Hopper, Estafet, Dopamine и други
* Активна общност с разнообразни и стабилни проекти с отворен код
  - Typelevel екосистема – Cats, Cats Effect, Http4s, fs2, doobie и много други
  - Kafka – distributed event streaming
  - Spark – big data & ML
  - Lightbend екосистема – Akka, Play Framework, Lagom, Slick

:::

# Какво ще учим 😊?<br />(част 1)

* Въведение във функционалното програмиране
* Основни подходи при функционално програмиране
  - рекурсия
  - неизменими (immutable) структури от данни
  - функциите като първокласни обекти
  - композиция на функции
  - изразяване чрез типове
* ООП
* Трансформиране на данни
* Lazy изчисления
* Дизайн на типове чрез ФП

# Какво ще учим 😊?<br />(част 2)

* Овладяване на страничните ефекти
* Функционална обработка на грешки
* Конкурентност, асинхронност, комуникация по мрежа (HTTP)
* Абстракции от по-висок ред – видове type classes
* Библиотечка за абстракции – Cats 😺
* Библиотечка за асинхронност и конкурентност – Cats Effect
* Изграждане на цялостнно Scala приложение
  - структура
  - управление на ресурсите
  - домейн дизайн
  - връзка с релационна база
  - конкурентност и асинхронност
  - HTTP

# Какво ще учим 😊?<br />Разпределение по теми

1. Въведение във функционалното програмиране със Scala
2. ООП във функционален език
3. Основни подходи при функционалното програмиране
4. Fold, колекции, lazy колекции
5. Pattern matching и алгебрични типове от данни (ADTs)
6. Ефекти и функционална обработка на грешки
7. Конкурентност
8. Type classes
9. Монади и апликативи
10. Cats и Cats Effect
11. Изграждане на Scala приложение

# Ресурси

* Functional Progamming in Scala ([1st edition](https://www.manning.com/books/functional-programming-in-scala), [2nd edition](https://www.manning.com/books/functional-programming-in-scala-second-edition))
* [Programming in Scala 5th edition](https://www.artima.com/shop/programming_in_scala_5ed)
* [Scala with Cats](https://www.scalawithcats.com/)
* Ресурси онлайн, [включително официалния сайт](https://docs.scala-lang.org/scala3/book/introduction.html)
* [API документация](https://scala-lang.org/api/3.x/)

# Въпроси :)?

::: { .fragment }

![](images/01-intro/case-ended.webp)

:::

