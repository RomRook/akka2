import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory


object Main extends App {

  val timeout = 1000L
  val logger = Logger(LoggerFactory.getLogger(this.getClass))
  val system = ActorSystem("the-actor-system")
  val simpleActor: ActorRef = system.actorOf(Props[SimpleActor])
  //val drinkActor: ActorRef = system.actorOf(Props[DrinkActor])

  val xml = <Locations>
              <Location id="1"></Location>
              <Location id="2"></Location>
              <Location id="3"></Location>
              <Location id="4"></Location>
              <Location id="5"></Location>
              <Location id="6"></Location>
            </Locations>

  val locNodes = xml \\ "Location"

  logger.info(locNodes.toString())
  logger.info("-----------------------------rg-----------------------")
  //logger.info(locNodes)

  val props = Props[DrinkActor]
  val sendActorProps = Props[SendActor]


  var list : List[ActorRef] = List.empty

  for( i <- 1 to 3) {
    val actorName = "sendActor-" + i
    val sendActor = system.actorOf(sendActorProps, actorName)
    list = sendActor :: list
  }

  logger.info(list.toString())
  logger.info(locNodes.head.toString())
  logger.info(locNodes.head.getClass.toString)

  val senders = list.size

  var index: Int = 0
 locNodes.foreach(n  => {
   index += 1
   val senderIndex = index % senders
   list(senderIndex) ! n
   //list(0) ! n
   //list(1) ! "ddd"
  })




//  list(0) ! locNodes.head

 // list(0) ! "fff"


  val drinkActor = system.actorOf(props, "drinkActor-1")

  drinkActor ! "tea"
  drinkActor ! "coffee"
  drinkActor ! "water"

  //simpleActor ! "Hello world"

  // Wait for message processing and shutdown
  // (not needed in normal application)
  java.lang.Thread.sleep(timeout)

  //simpleActor ! Ping()

  //drinkActor ! "tea"

  system.terminate()
}
