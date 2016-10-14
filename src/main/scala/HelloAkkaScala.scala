import akka.actor.{Actor, ActorRef, ActorSystem, Inbox, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.pattern.ask
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Multipart.BodyPart

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn

case object Greet
case class WhoToGreet(who: String)
case class Greeting(message: String)
case class ColorAndAge(color: String, age: Int)

class Greeter extends Actor {
  var greeting = ""

  def receive = {
    case WhoToGreet(who) =>
      println("Received $who")
      sender ! Greeting(s"hello, $who")
  }
}

object HelloAkkaScala {

  def main(args: Array[String]) {
    // Create the 'helloakka' actor system
    implicit val system = ActorSystem("helloakka")

    // Create the 'greeter' actor
    val greeter = system.actorOf(Props[Greeter], "greeter")

    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    implicit val askTimeout: Timeout = 3.seconds

    implicit val GreetingFormat = jsonFormat1(Greeting)

    val blah = Map("hi" -> List("there", "foo"), "foo" -> List("bar", "huH"))

    implicit val ColorAndAgeFormat = jsonFormat2(ColorAndAge)

    val route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      } ~
      path("workflows" / Segment / "backends") { version =>
        get {
          val greeting: Future[Greeting] = greeter.ask(WhoToGreet("bob")).mapTo[Greeting]
          complete(greeting)
        }
      } ~
    path("workflows" / Segment / "foo") { version =>
      complete(blah)
    } ~
    path("multipart") {
      post {
        entity(as[Multipart.FormData]) { shite =>
          val allParts: Future[Map[String, Any]] = shite.parts.mapAsync[(String, Any)](1) {
            case b: BodyPart => b.toStrict(5.seconds).map(strict => b.name -> strict.entity.data.utf8String)
          }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)
          onSuccess(allParts) { x =>
            println(x)
            complete { "hi" }
          }
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    while (true) {
      // stupid way to block the main thread so bindingFuture doesn't complete
      Thread.sleep(1000)
    }
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
