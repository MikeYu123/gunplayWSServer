package online.gunplay.graphics

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive
/**
  * Created by mike on 18.08.16.
  */
object GxStage{
  case class RemoveChild(uuid: String)
  case object Step
  val playerClass = GxPlayer.getClass
  val doorClass = GxDoor.getClass
  val wallClass = GxWall.getClass
  case object Update
  case class AddPlayer(name: String)
}

class GxStage(playerSpawns: List[PlayerSpawnDefinition], itemSpawns: List[ItemSpawnDefinition], doors: List[DoorDefinition], walls: List[WallDefinition]) extends Actor{
  import org.jbox2d.common.Vec2
  import org.jbox2d.dynamics.World
  import scala.util.Random
  import GxStage._

//  TODO: App config???
  val frame_per_second = 50
  val time_step = 1.0f / frame_per_second
  val velocity_iterations = 6
  val position_iterations = 2
  var id_seed = 0
  val world = new World(new Vec2(0, 0))

  walls.foreach(wallDef => {
    val uuid = UUID.randomUUID().toString()
    val actorRef = context.actorOf(Props(wallClass, world, uuid, wallDef.sizes, wallDef.position))
    children += (uuid -> actorRef)
  })

  doors.foreach(doorDef => {
    val uuid = UUID.randomUUID().toString()
    val actorRef = context.actorOf(Props(doorClass, world, uuid, doorDef.sizes, doorDef.orientation, doorDef.position))
    children += (uuid -> actorRef)
  })

  val contactListener = new GxContactListener(this)
  world.setAllowSleep(true)
  val children = new collection.mutable.HashMap[String, ActorRef]()


  def addPlayer(name: String): Unit = {
    val uuid = UUID.randomUUID().toString()
    val spawn = getRandomPlayerSpawn.position.toVec2
    val actorRef = context.actorOf(Props(playerClass, world, uuid, spawn, name))
    children += (uuid -> actorRef)
  }

  def removeChild(uuid: String) : Unit = {
    children.remove(uuid)
  }

  //Initialized from Json
  def getRandomItemSpawn: ItemSpawnDefinition = {
    itemSpawns(Random.nextInt(itemSpawns.size))
  }

  def getRandomPlayerSpawn: PlayerSpawnDefinition = {
    playerSpawns(Random.nextInt(playerSpawns.size))
  }

  def step() {
    world.step(time_step, velocity_iterations, position_iterations)
    children.values.foreach(_ ! Update)
  }

  override def receive : Receive = {
    case RemoveChild(uuid) =>
      removeChild(uuid)
    case AddPlayer(name) =>
      addPlayer(name)
    case Step =>
      step()
    }
}
