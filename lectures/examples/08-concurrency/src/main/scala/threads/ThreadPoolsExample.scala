package threads

import java.util.concurrent.{Executor, ExecutorService, Executors, ForkJoinPool}

@main def runThreadPoolsExample =
  val threadPool: Executor = new ForkJoinPool()

  threadPool.execute(() => {
    println("Hellooo")
    threadPool.execute(() => println("Additional work"))
  })

  threadPool.execute(() => println("Running concurrently"))

  // In a real application we will wait for all the work to finish
  // and the we will stop the thread pool
  // Here we just sleep to simulate waiting.
  // Later in the course we will see haw we can achieve the above
  // much easier with functional programming using IO resources
  Thread.sleep(500)
end runThreadPoolsExample
