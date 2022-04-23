package http

import concurrent.future.{Future, Promise}
import concurrent.io.IO
import org.asynchttpclient.Dsl.*
import org.asynchttpclient.*

import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext
import scala.util.Try

object HttpClient:
  val client = asyncHttpClient()

  def get(url: String): Future[Response] =
    val p = Promise[Response]

    println("Getting " + url)

    val eventualResponse = client.prepareGet(url).setFollowRedirect(true).execute()
    eventualResponse.addListener(() => p.complete(Try(eventualResponse.get())), null)
    eventualResponse.addListener(() => println(s"Finished getting $url"), null)

    p.future

  def getIO(url: String): IO[Response] =
    val eventualResponse = client.prepareGet(url).setFollowRedirect(true).execute()

    IO.usingCallback[Response] { (ec, callback) =>
      eventualResponse.addListener(() => callback(Try(eventualResponse.get())), r => ec.execute(r))
    }

  def getScalaFuture(url: String): scala.concurrent.Future[Response] =
    val p = scala.concurrent.Promise[Response]()

    val eventualResponse = client.prepareGet(url).setFollowRedirect(true).execute()
    eventualResponse.addListener(() => p.complete(Try(eventualResponse.get())), null)

    p.future

case class BadResponse(statusCode: Int) extends Exception
