package streams

import cats.effect.*
import fs2.{Pipe, Stream}
import org.http4s.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.server.websocket.{WebSocketBuilder, WebSocketBuilder2}
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.Text

import scala.concurrent.ExecutionContext.global

object Fs205WebSocket extends IOApp.Simple:
  def routes(wsBuilder: WebSocketBuilder2[IO]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "echo-ws" =>
      val echoReply: Pipe[IO, WebSocketFrame, WebSocketFrame] =
        _.flatMap {
          case Text(msg, _) =>
            Stream(
              Text(s"You sent the server: $msg."),
              Text("Yay :)")
            )
          case _ => Stream(Text("You sent something different than text"))
        }

      wsBuilder.build(echoReply)
  }

  def httpApp(wsBuilder: WebSocketBuilder2[IO]) = routes(wsBuilder).orNotFound

  val serverBuilder = BlazeServerBuilder[IO]
    .bindHttp(8080, "localhost")
    .withHttpWebSocketApp(httpApp)
    .resource

  def run: IO[Unit] = serverBuilder.use(_ => IO.never)
