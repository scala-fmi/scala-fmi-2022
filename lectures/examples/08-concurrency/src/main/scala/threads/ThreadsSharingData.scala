package threads

@main def runThreadsSharingDataExample =
  // Ако тази променлива не беше маркирана като @volatile,
  // то нишката thread по-долу нямаше да вижда промените по нея
  @volatile var improveCalculation = true

  val thread = new Thread(() =>
    var i = 0L

    while improveCalculation do
      i += 1

    println(s"Thread exiting: $i")
  )

  thread.start()

  println("Main going to sleep...")
  Thread.sleep(1000)
  println("Main waking up...")

  improveCalculation = false

  thread.join()

  println("Main exiting")
end runThreadsSharingDataExample
