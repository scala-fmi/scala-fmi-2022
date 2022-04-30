package http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import library.{BookId, Library}
import util.Utils

import scala.concurrent.{ExecutionContext, Future}

object FutureWebServer:
  given actorSystem: ActorSystem = ActorSystem()
  given ec: ExecutionContext = actorSystem.dispatcher

  def doWork() = Future {
    Utils.doWork
    Utils.doWork

    42
  }

  val routes: Route = (get & path("do-work")) {
    complete(doWork().map(_.toString))
  }

  def main(args: Array[String]): Unit =
    Http().newServerAt("0.0.0.0", 8080).bindFlow(routes)
