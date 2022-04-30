package answers

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.*
import library.{AuthorId, BookId, Library}
import util.Utils
import Library.TheGreatLibrary
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContext, Future}

object LibraryWebServer:
  given actorSystem: ActorSystem = ActorSystem()
  given ec: ExecutionContext = actorSystem.dispatcher

  def doWork(): Future[Int] = Future {
    Utils.doWork
    Utils.doWork

    42
  }

  val routes: Route = (get & path("do-work")) {
    complete(doWork().map(_.toString))
  } ~ get {
    library
  }

  def library: Route = concat(
    listBooks,
    findBookName,
    findBookGenre,
    findBookAuthors,
    findAuthorName
  )

  def listBooks =
    path("books") {
      complete {
        TheGreatLibrary.allBooks.map(bookIds => bookIds.map(_.id).mkString(","))
      }
    }

  def findBookName =
    path("books" / Segment / "name") { bookIdString =>
      complete {
        TheGreatLibrary
          .findBook(BookId(bookIdString))
          .map(toEntityResponse(_.name))
      }
    }

  def findBookGenre =
    path("books" / Segment / "genre") { bookIdString =>
      complete {
        TheGreatLibrary
          .findBook(BookId(bookIdString))
          .map(toEntityResponse(_.genre))
      }
    }

  def findBookAuthors =
    path("books" / Segment / "authors") { bookIdString =>
      complete {
        TheGreatLibrary
          .findBook(BookId(bookIdString))
          .map(toEntityResponse(_.authors.map(_.id).mkString(",")))
      }
    }

  def findAuthorName =
    path("authors" / Segment / "name") { authorIdString =>
      complete {
        TheGreatLibrary
          .findAuthor(AuthorId(authorIdString))
          .map(toEntityResponse(_.name))
      }
    }

  def toEntityResponse[A](f: A => String)(maybeEntity: Option[A]) =
    maybeEntity
      .map(f)
      .map(s => HttpResponse(entity = s))
      .getOrElse(HttpResponse(StatusCodes.NotFound))

  def main(args: Array[String]): Unit =
    Http().newServerAt("0.0.0.0", 8080).bindFlow(routes)
