package answers.multiparameters

import answers.TailRecursion.{fact, fibonacci}
import answers.multiparameters.TypeInference.mapML

import scala.annotation.tailrec
import scala.io.Source
import scala.language.reflectiveCalls

object LanguageConstructs extends App:
  // times construct
  extension (n: Int)
    @tailrec
    def times(fn: => Unit): Unit =
      if n <= 0 then ()
      else
        fn
        (n - 1).times(fn)

  4.times {
    println("Meow")
    println("I am a cat")
  }

  // extension methods can also be called this way:
  times(4) {
    println("Meow")
  }
  // They actually are functions with multiple parameters lists where the first one consists of the target object.
  // Be being extension methods Scala allows to call them as methods on the target object.

  @tailrec
  def timesN(n: Int)(fn: => Unit): Unit =
    if n <= 0 then ()
    else
      fn
      timesN(n - 1)(fn)

  timesN(4) {
    println("Meow")
    println("I am a cat")
  }

  // Higher-ordered functions as constructs
  mapML(List(4, 5, 6)) { n =>
    val factN = fact(n)
    val fibN = fibonacci(n)

    factN + fibN
  }

  List(20, -40, 30).fold(0) { (a, b) =>
    math.max(a.abs, b.abs)
  }

  // safe resources
  type Closeable = { def close(): Unit }

  def using[A <: Closeable, B](resource: A)(f: A => B): B =
    try f(resource)
    finally resource.close() // requires the reflectiveCalls import. Using java.lang.AutoCloseable might be better here

  def numberOfLines(fileName: String): Int =
    using(Source.fromFile(fileName)) { file =>
      file.getLines().size
    }

  println {
    numberOfLines("build.sbt")
  }
