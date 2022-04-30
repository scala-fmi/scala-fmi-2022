package exercises

@main def multiParameterListsExamples =
  def min[T](compare: (T, T) => Int)(a: T, b: T) =
    if compare(a, b) <= 0 then a
    else b

  def compareByAbsoluteValue(a: Int, b: Int) = a.abs - b.abs

  val minByAbsoluteValue = min(compareByAbsoluteValue)

  println {
    minByAbsoluteValue(10, -20)
  }

  println {
    List(-10, -30, 2, 8).reduce(minByAbsoluteValue)
  }

  extension (n: Int)
    def times(block: => Unit): Unit =
      if n == 0 then ()
      else
        block
        (n - 1).times(block)

//  def times(n: Int)(block: => Unit): Unit =
//    if n == 0 then ()
//    else
//      block
//      times(n - 1)(block)

  4.times {
    println("Meow")
  }

  // extension methods can also be called this way:
  times(4) {
    println("Meow")
  }
  // They actually are functions with multiple parameters lists where the first one consists of the target object.
  // Be being extension methods Scala allows to call them as methods on the target object.
end multiParameterListsExamples
