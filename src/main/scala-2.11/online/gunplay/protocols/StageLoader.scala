package online.gunplay.protocols

import java.io.InputStream

import online.gunplay.graphics._
import spray.json.DefaultJsonProtocol
import spray.json._

import scala.io.Source

/**
  * Created by miks on 21.08.16.
  */
object StageLoader extends DefaultJsonProtocol {
  case class WallDescription(x: Float, y: Float, w: Float, h: Float)
  case class DoorDescription(x: Float, y: Float, w: Float, h: Float, o: Int)
  case class SpawnDescription(x: Float, y: Float)
  case class SpawnsDescription(players: List[SpawnDescription], items: Option[List[SpawnDescription]])
  case class StageDescription(walls: List[WallDescription], doors: List[DoorDescription], spawns: SpawnsDescription)
  val pixelsPerMeter = 200.0f

  implicit val wallFormat = jsonFormat4(WallDescription)
  implicit val doorFormat = jsonFormat5(DoorDescription)
  implicit val spawnFormat = jsonFormat2(SpawnDescription)
  implicit val spawnsFormat = jsonFormat2(SpawnsDescription)
  implicit val stageFormat = jsonFormat3(StageDescription)

  def readFile(path: String) : StageDescription = {
    val document = Source.fromFile(path).mkString
    val description = document.parseJson.convertTo[StageDescription]
    println(description)
    description
  }

  def readStream(stream: InputStream) : StageDescription = {
    val document = Source.fromInputStream(stream).mkString
    val description = document.parseJson.convertTo[StageDescription]
    println(description)
    description
  }

  def readResource(path: String) : StageDescription = {
    val stream = getClass.getResourceAsStream(path)
    readStream(stream)
  }

  def doorDefinitions(stageDescription: StageDescription): List[DoorDefinition] = {
    stageDescription.doors.map(dDesc => {
      DoorDefinition(
        Location(dDesc.x / pixelsPerMeter, dDesc.y / pixelsPerMeter),
        RectSizes(dDesc.w / pixelsPerMeter, dDesc.h / pixelsPerMeter),
        DoorOrientation(dDesc.o)
      )
    })
  }

  def wallDefinitions(stageDescription: StageDescription): List[WallDefinition] = {
    stageDescription.walls.map(dDesc => {
      WallDefinition(
        Location(dDesc.x / pixelsPerMeter, dDesc.y / pixelsPerMeter),
        RectSizes(dDesc.w / pixelsPerMeter, dDesc.h / pixelsPerMeter)
      )
    })
  }

  def itemSpawnDefinitions(stageDescription: StageDescription): List[ItemSpawnDefinition] = {
    stageDescription.spawns.items match {
      case Some(dDesc: List[SpawnDescription]) =>
        dDesc.map(description => ItemSpawnDefinition(Location(description.x / pixelsPerMeter, description.y / pixelsPerMeter)))
      case None =>
        List[ItemSpawnDefinition]()
    }
  }

  def playerSpawnDefinitions(stageDescription: StageDescription): List[PlayerSpawnDefinition] = {
    stageDescription.spawns.players.map(dDesc => {
      PlayerSpawnDefinition(
        Location(dDesc.x / pixelsPerMeter, dDesc.y / pixelsPerMeter)
      )
    })
  }
}
