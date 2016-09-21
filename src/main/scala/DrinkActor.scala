import akka.actor.Actor
import akka.event.Logging

class DrinkActor extends Actor {
  val log = Logging(context.system, this)
  def receive()  = {
    case "tea" => log.info("Tea time!")
    case "coffee" => log.info("Coffee time!")
    case _ => log.info("Hmmm...")
  }
}
