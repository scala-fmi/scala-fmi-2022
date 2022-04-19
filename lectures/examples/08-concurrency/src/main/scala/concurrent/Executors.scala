package concurrent

import java.util.concurrent.{Executor, ForkJoinPool}

object Executors:
  given Executor = new ForkJoinPool

  val currentThreadExecutor = new Executor:
    override def execute(operation: Runnable): Unit = operation.run()

  val blockingExecutor = java.util.concurrent.Executors.newCachedThreadPool()
