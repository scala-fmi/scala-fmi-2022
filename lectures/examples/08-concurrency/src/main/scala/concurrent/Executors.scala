package concurrent

import java.util.concurrent.{Executor, ExecutorService, ForkJoinPool}

object Executors:
  given defaultExecutor: Executor = new ForkJoinPool

  val currentThreadExecutor: Executor = new Executor:
    def execute(operation: Runnable): Unit = operation.run()

  val blockingExecutor: ExecutorService = java.util.concurrent.Executors.newCachedThreadPool()
