package threads

@main def runThreadsSharingDataExample2 =
  @volatile var improveCalculation = true

  var result: Option[Int] = None
  @volatile var thread1Ready = false

  val thread1 = new Thread(() => {
    var i = 0

    while improveCalculation do
      i += 1

    result = Some(i)
    // writing to the volatile variable guarantees that
    // thread two will see the value of result we set above
    thread1Ready = true

    // if we change result here thread2 is not guaranteed to see it
    // even if it wakes up after we've made the change
    // result = Some(1024)

    println(s"Thread 1 exiting: $i")
  })

  val thread2 = new Thread(() => {
    while !thread1Ready do
      Thread.sleep(10)

    println(s"Thread 2 exiting: $result")
  })

  thread1.start()
  thread2.start()

  println("Main going to sleep...")
  Thread.sleep(1000)
  println("Main waking up...")

  improveCalculation = false

  thread1.join()
  thread2.join()

  println("Main exiting")
end runThreadsSharingDataExample2
