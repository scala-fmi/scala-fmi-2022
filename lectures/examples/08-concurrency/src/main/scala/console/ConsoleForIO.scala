package console

import concurrent.io.IO

import scala.concurrent.ExecutionContext
import scala.io.StdIn

class ConsoleForIO(blockingEc: ExecutionContext):
  // we want the blocking console operations to be executing in the blocking execution context
  def getStringLine: IO[String] = IO(StdIn.readLine()).bindTo(blockingEc)
  def putStringLine(str: String): IO[Unit] = IO(println(str)).bindTo(blockingEc)
