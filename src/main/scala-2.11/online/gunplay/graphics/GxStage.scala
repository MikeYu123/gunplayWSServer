package online.gunplay.graphics

import online.gunplay.graphics.GxSpawnPlace.{ItemSpawn, PlayerSpawn, SpawnPlace}

/**
  * Created by mike on 18.08.16.
  */
class GxStage(playerSpawns: List[PlayerSpawn], itemSpawns: List[ItemSpawn]) {
  import org.jbox2d.common.Vec2
  import org.jbox2d.dynamics.World
  import scala.util.Random

  val frame_per_second = 50
  val time_step = 1.0f / frame_per_second
  val velocity_iterations = 6
  val position_iterations = 2
  var id_seed = 0
  val world = new World(new Vec2(0, 0))
  world.setAllowSleep(true)
  val children = new collection.mutable.HashMap[Long, GxObject]()

//  def addPlayer() {
//    val id = id_seed
//    val pl: GxPlayer = new GxPlayer(this, id)
//    children(id) = pl
//  }

  //Initialized from Yaml
    def getRandomItemSpawn: ItemSpawn = {
      itemSpawns(Random.nextInt(itemSpawns.size))
    }

    def getPlayerItemSpawn: ItemSpawn = {
      itemSpawns(Random.nextInt(itemSpawns.size))
    }

//  TODO: IdGeneratingActor
    def generateId: Long = {
      id_seed += 1
      id_seed - 1
    }

    def step() {
      this.world.step(this.time_step, this.velocity_iterations, this.position_iterations)
    }

}
