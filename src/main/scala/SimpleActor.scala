import akka.actor.Actor
import akka.event.Logging

case class Ping()
case class Pong()

class SimpleActor extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case x: Ping => log.info("Received Ping object")
    case x =>
      log.info("Received message: " + x)
  }
}
