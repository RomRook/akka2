import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.event.Logging
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import akka.actor._


case class ActorStatus(actorRef: ActorRef)

class MainActor extends Actor {
	val log = Logging(context.system, this)
	var totalCount = 0

	def receive() = {
		case ActorStatus(actor) =>  {println("sendinig message to actor")
			actor ! "status_1" }
		case Status(count) => {
      println(s"recived from $sender $count")
			totalCount += count
			println("all: " + totalCount)
		}
		case _ => log.info("Hmmm...")
	}
}

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


	var list: List[ActorRef] = List.empty



	for (i <- 1 to 3) {
		val actorName = "sendActor-" + i
		val sendActor = system.actorOf(sendActorProps, actorName)
		list = sendActor :: list
	}

	logger.info(list.toString())
	logger.info(locNodes.head.toString())
	logger.info(locNodes.head.getClass.toString)

	val senders = list.size

	var index: Int = 0
	locNodes.foreach(n => {
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


	val mainActor = system.actorOf(Props(new MainActor()), "main-actor")

	mainActor ! ActorStatus(drinkActor)
  mainActor ! ActorStatus(drinkActor)

  drinkActor ! "tea"
  mainActor ! ActorStatus(drinkActor)



	//simpleActor ! "Hello world"

	// Wait for message processing and shutdown
	// (not needed in normal application)
	java.lang.Thread.sleep(4000)

	//  //dsddddf
	//  ///


	//simpleActor ! Ping()

  import scala.concurrent.{Await, ExecutionContext, Future}
  import scala.concurrent.duration._
  import akka.pattern.ask
  import akka.pattern.gracefulStop
  import akka.util.Timeout

	drinkActor ? "tea"
  implicit val timeout2 = Timeout(5 seconds)
  val future = drinkActor ? "Status"
  val result = Await.result(future, timeout2.duration).asInstanceOf[String]
  println(result)

  drinkActor

  import scala.language.postfixOps
  val future2: Future[String] = ask(drinkActor, "Status").mapTo[String]
  val result2 = Await.result(future2, 1 second)
  println(result2)

  val future3: Future[Boolean] = gracefulStop(drinkActor,timeout2.duration, "message")
  val result3 = Await.result(future3, 1 second)
  println(result3)

  system.shutdown

	val rg = system.actorSelection("/user/drinkActor-1")
	//val rg = system.actorSelection("/user/Parent/Jonathan")
	println("rg" + rg.toString())

	rg ! "tea"
	//rg ! PoisonPill

	rg ! "status"
	rg ! "tea"

	rg ! "status"

	system.terminate()

	//rg ! "tea"
	//rg ! "tea"

	val rg1 = system.actorSelection("/user/drinkActor-1")



	//rg1 ! "tea"

	println("---end-------")

	list
}
