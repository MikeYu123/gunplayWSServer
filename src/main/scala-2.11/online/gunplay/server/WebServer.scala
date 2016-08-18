import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.Flow
import scala.io.StdIn

val echoService: Flow[Message, Message, _] = Flow[Message].map {
  case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
  case _ => TextMessage("Message type unsupported")
}

object WebServer extends App {

  implicit val actorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  import akka.http.scaladsl.server.Directives._

  val route = get {
    pathEndOrSingleSlash {
      complete("Welcome to websocket server")
    }
  } ~
  path("ws-echo") {
    get {
      akka.http.scaladsl.server.Directives.handleWebSocketMessages(echoService)
    }
  }
  val binding =
    Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  StdIn.readLine()

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.shutdown())
  println("Server is down...")

}

