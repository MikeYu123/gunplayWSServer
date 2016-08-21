package online.gunplay

import org.jbox2d.common.Vec2


package object graphics {
  import DoorOrientation.DoorOrientation
  case class RectSizes(width: Float, height: Float)
  case class Location(x: Float, y: Float) {
    def toVec2 = new Vec2(x, y)
  }
  object DoorOrientation extends Enumeration {
    type DoorOrientation = Value
    val Up = Value(3)
    val Right = Value(0)
    val Down = Value(1)
    val Left = Value(2)
  }
  case class WallDefinition(position: Location, sizes: RectSizes)
  case class DoorDefinition(position: Location, sizes: RectSizes, orientation: DoorOrientation)
  case class PlayerSpawnDefinition(position: Location)
  case class ItemSpawnDefinition(position: Location)
}