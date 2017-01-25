import akka.actor.Actor
import akka.event.Logging

case class Status(count: Int)

class DrinkActor extends Actor {
  val log = Logging(context.system, this)

  var teaCount = 0

  def receive() = {
    case "tea" => {log.info("Tea time!")
      teaCount += 1
      println("teaCount: " + teaCount )
      sender ! Status(teaCount)
    }
    case "coffee" => log.info("Coffee time!")
    case "status" => println("Status for tea: " + teaCount )
    case "status_1" => {
      println("Status from MainActor : " + teaCount )
      sender ! Status(teaCount)
    }
    case _ => log.info("Hmmm...")
  }
}
