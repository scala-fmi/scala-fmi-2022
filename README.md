# Функционално програмиране за напреднали със Scala

## Лекции

* [01 – За курса](https://scala-fmi.github.io/scala-fmi-2022/lectures/01-intro.html)
  - Кои сме ние?
  - Административни неща
  - Защо функционално? Кратка история на функционалното
  - Защо Scala?
  - Какво ще учим?
  - Ресурси
* [02 – Въведение във функционалното програмиране със Scala](https://scala-fmi.github.io/scala-fmi-2022/lectures/02-fp-with-scala.html) \[[код](lectures/examples/02-fp-with-scala)\]
  - Инсталиране на Scala
  - Scala инструменти
    * `scala`, `scalac`, REPL
    * IDE. IntelliJ Idea и Metals
    * sbt, конфигурация, компилиране, стартиране и тестване
    * добавяне на библиотеки
  - Типове и литерали
  - Дефиниции – `val`, `var`, `def`, `type`, функции. Type inference
  - Файлове и пакети
  - Стойностите като обекти (с методи)
  - `if` контролна структура
  - "Всичко е израз или дефиниция". Контролните структури и блоквете като изрази
  - Типова йеархия. `Any`, `AnyVal`, `AnyRef`, `Unit`, `Null`, `Nothing`
  - Java Memory Model
  - Контролни структури със странични ефекти – `while`, `try/catch` (и изключения), `for`
  - Още контролни структури без странични ефекти – pattern matching, (функционален) `for`
  - Съставни типове
    * хетерогенни – наредени n–торки (tuples)
    * хомогенни – `Range`, `List[A]`, `Set[A]`, `Map[K, V]`
  - Операции над хомогенни колекции – `isEmpty`, `head`, `tail`, `take`, `drop`
  - Scala 3 и Scala 2 синтаксис
  - Помощен синтаксис при функции
    * типови параметри (generics)
    * overloading
    * default стйности на параметри
    * именувани аргументи
    * променлив брой параметри
  - Функционално програмиране
    * математически функции
    * функционално срещу императивно програмиране
    * характеристики на функционалните програми
    * Substitution модел на изчисление. Предаване на параметрите по стойност и по име
    * Референтна прозрачност
* [03 – ООП във функционален език](https://scala-fmi.github.io/scala-fmi-2022/lectures/03-oop-in-a-functional-language.html) \[[код](lectures/examples/03-oop-in-a-functional-language)\]
  - Обектно-ориентирано програмиране като система от обекти
    * Съобщения, енкапсулация и late-binding
    * ООП като концепция, ортогонална на ФП. ООП в Scala
  - Дефиниране на клас. Параметри на клас (конструктори), членове, модификатори на достъп
  - Дефиниране на обект
  - `apply` метод. Обекти, приложими като функции
  - Придружаващи обекти
  - implicit конверсии
  - `case` класове за дефиниране на неизминими value обекти
  - Абстрактни типове – trait
    * Uniform Access Principle
    * множествено наследяване
    * подтипов полиморфизъм
  - `import` и `export` клаузи
  - Обвиващи `AnyVal` класове. Type safety чрез обвиване на обикновени типове
  - Изброени типове
  - Съвместимост на типове. Номинално и Структурно типизиране
  - Структурно типизиране в Scala
  - Типова алгебра. Обединение и сечение на типове
  - The Expression Problem в Scala. ООП срещу ФП конструкции
  - Extension методи – ретроактивно добавяне на методи към тип
  - Тестове
  - ООП дизайн
* [04 – Основни подходи при функционалното програмиране](https://scala-fmi.github.io/scala-fmi-2022/lectures/04-key-fp-approaches.html) \[[код](lectures/examples/04-key-fp-approaches)\]
  - Рекурсия
    * итерация чрез рекурсия
    * рекурсия и substitution модела
    * опашкова рекурсия. Рекурсия чрез акумулатори
    * рекурсия по колекции
  - Функциите като първокласни обекти
    * ламбда синтаксис и функционален тип
    * преобразуване на `def` функции/методи към обектни функции – eta expansion
    * частично прилагане на аргументи
    * функции от по-висок ред. `filter`, `map`, `reduce`, `fold`
    * композиция на функции от по-висок ред
    * множество списъци с параметри
      - групиране на параметри
      - type inference
      - създаване на собствени конструкции
      - currying
    * Операции с функции. Композиция, преобразуване към `curried` или `tupled` форми
  - Неизменимост и неизменими структури от данни
    * неизменими обекти във времето
    * persistence (персистентност) и structural sharing. Роля на Garbage Collector-а
    * персистентност и structural sharing при списък (`List`)
    * `Vector` – оптимизация за произволен достъп и добавяне на елементи в началото и в края. Ефективно константна сложност
    * чисто функционални структури от данни. Безопасно споделяне на такива структури между компоненти и нишки
    * `Set` и `Map` като чисто функционални структури
    * Наредените n-торки като чусто функционални структури. Операции
    * Други приложения на неизменимите структури от данни
      - в базите от данни (append-only logs, MVCC, STM и други)
      - Git – обектен модел, представляващ immutable snapshot на файлова система
      - UI и front-end (примери с Redux и virtual DOM)
  - Композиция на функции
  - Изразяване чрез типове
* [05 – Fold и колекции](lectures/05-folds-collections.ipynb)
  - **забележка**: примерите от тази лекция са на Scala 2 поради използването на Jupyter като презентационен инструмент
  - Абстракция чрез `fold`, `foldLeft` и `foldRight`. Изразяване на операции чрез тях
  - Йеархия и дизайн на колекциите в Scala.
    * Uniform Return Type principle
    * Колекциите като функции (примери за `Seq`, `Set`, `Map`)
  - Честични функции. Частични функции чрез `case` блокове
  - Още операции върху колекции – `partition`, `span`, `splitAt`; `flatten`, `flatMap`, `zip`, `groupBy` и други
  - Тайната за `for` конструкциите – синтактична захар върху `map`, `flatMap` и `withFilter` (`filter`)
  - Lazy колекции
    * нестриктни view-та на колекции
    * lazy списъци с мемоизация
  - Имплементация на `LazyList`
* 06 – Pattern matching и алгебрични типове от данни (ADTs)
  - Pattern matching. Деструктуриране на обекти
  - Pattern matching на различни места
    * `case` блокове – при `match` или на мястата, където се очакват функции или частични функции. Примери с `map` и `collect`
    * pattern bindings – съпоставяне с единичен pattern при `for` (отляво на генератори и дефиници) и при `val` дефиниции
  - Алгебрични типове от данни. Примери
    * product типове. `case` класове, наредени n-торки и други
    * sum типове. Имплементация чрез `sealed trait` и `enum`
  - Type bounds (ограничения върху типовите параметри).
  - Вариантност. Ковариантност и контравариантност на типове. Вариантност при типовете за функции. [Още примери](https://www.freecodecamp.org/news/understand-scala-variances-building-restaurants/)
  - Изразяване на опционалност. `null` като грешка за милиард долара. Имплементация на `Option` тип
  - Полезни операции върху `Option`. Използване на операции вместо pattern matching
  - Деструктуриране чрез екстрактори. Използването им в pattern matching
* 07 – Ефекти и функционална обработка на грешки
  - **забележка**: примерите от тази лекция са на Scala 2 поради използването на Jupyter като презентационен инструмент
  - Функционален дизайн – премахване на нелегалните състояния
  - От частични към тотални функции
  - Комплексност на типове
  - Ефекти
  - Видове ефекти – частичност, изключения/грешки, недетерминизъм, зависимости/конфигурация, логване, изменяемо състояние, вход/изход, асинхронност, и други. Типове зад тях
  - Проблемите при изключенията
  - Моделиране на грешни състояние чрез `Option`, `Try` и `Either`
    * замяна на изключенията с безопасни и композитни ефекти
    * моделиране на грешките като домейн структури и домейн логика
  - Ефект за вход/изход. IO. Предимства на IO
  - Разделение на чисто функционално композитно изграждане на план (без странични ефекти) от изпълнение на плана (водещо до странични ефекти)

## Допълнителни ресурси

* [Таблица на елементите, съставящи типовата система на Scala](resources/type-elements-in-scala.md)

## Генериране на лекции

### Setup

Имате нужда от инсталиран [pandoc](https://pandoc.org/installing.html).

Проектът има submodule зависимост към reveal.js. При/след клониране на репото инициализирайте модулите:

    git submodule update --init

### Генериране на конретна лекция

    cd lectures
    ./generate-presentation.sh <лекция>

### Генериране на всички лекции

    cd lectures
    ./build.sh
