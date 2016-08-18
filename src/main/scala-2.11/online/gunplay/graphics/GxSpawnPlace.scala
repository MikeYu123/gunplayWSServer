package online.gunplay.graphics

import org.jbox2d.common.Vec2

/**
  * Created by mike on 18.08.16.
  */
object GxSpawnPlace {
  class SpawnPlace(stage: GxStage, position: Vec2)
  case class PlayerSpawn(stage: GxStage, position: Vec2) extends SpawnPlace(stage, position)
  case class ItemSpawn(stage: GxStage, position: Vec2) extends SpawnPlace(stage, position)
}
