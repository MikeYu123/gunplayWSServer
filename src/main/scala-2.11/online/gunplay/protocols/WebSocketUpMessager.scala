package online.gunplay.protocols

import spray.json._
/**
  * Created by miks on 21.08.16.
  */
object WebSocketUpMessager extends DefaultJsonProtocol{
  case class ObjectInfo(uuid: String, objectType: String, x: Int, y: Int, angle: Float)
  case class WorldInfo(objects: List[ObjectInfo])
  implicit val objectFormat = jsonFormat5(ObjectInfo)
  implicit val worldFormat = jsonFormat1(WorldInfo)
}
