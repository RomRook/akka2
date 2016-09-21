import akka.actor.Actor
import akka.event.Logging

import scala.xml.NodeSeq

class SendActor extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case n: NodeSeq => {
      log.info(n.toString())
    }
    case _ => log.info("nie wiem")
  }
}
