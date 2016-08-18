package online.gunplay.graphics

import akka.actor.Actor
import akka.actor.Actor.Receive
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body

/**
  * Created by mike on 18.08.16.
  */

object GxObject{
  abstract class ObjectData(uuid: String)
}

//TODO: utilize akka parent-child relations when migrating to akka actors
abstract class GxObject(val stage: GxStage, val uuid: String) extends Actor{
  val angle: Float =  .0f
  val position: Vec2 = new Vec2(0, 0)
  val body: Body
  def getuuid: String = this.uuid

  def getBody: Body = this.body

  def getAngle: Float = this.angle

  def getPosition: Vec2 = this.position

  def destroyBody() {
//    TODO: refactor pls
    if (body != null) {
      stage.world.destroyBody(body)
//      body = null
    }
  }
}
