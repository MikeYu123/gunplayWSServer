package online.gunplay.graphics

import akka.actor.Actor.Receive
import akka.actor.ActorRef
import online.gunplay.graphics.GxObject.ObjectData
import org.jbox2d.collision.shapes.{CircleShape, PolygonShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics._

import scala.math.{cos, sin}
/**
  * Created by mike on 18.08.16.
  */
object GxBullet {
  val bullet: Boolean = true
  val bodyType: BodyType = BodyType.DYNAMIC
  val radius: Float = 0.004f
  val density: Float = 0.0f
  val groupIndex: Int = -3
  val bodyFixedRotation: Boolean = true
  val dmg: Float = 0.0f
  case class BulletObjectData(uuid: String, damage: Float) extends ObjectData(uuid)
  case object Destroyed
  case object Killed
}

class GxBullet(override val world: World, override val uuid: String, override val position: Vec2, initialAngle: Float)
  extends GxObject(world, uuid){
  import GxBullet._
  angle = initialAngle
  override val body: Body = presetBody(position, angle)
  val filter: Filter = presetFilter()
  val shape: Shape = presetShape(radius)
  presetFixture(filter, body, shape)

  private def presetBody(position: Vec2, angle: Float): Body = {
    val bodyDefinition: BodyDef = new BodyDef()
    bodyDefinition.bullet = bullet
    bodyDefinition.position = position
    bodyDefinition.angle = angle
    bodyDefinition.fixedRotation = bodyFixedRotation
    bodyDefinition.`type` = bodyType
    bodyDefinition.userData = BulletObjectData(uuid, dmg)
    world.createBody(bodyDefinition)
  }

  private def presetFilter() : Filter = {
    val filter = new Filter()
    filter.groupIndex = groupIndex
    filter
  }


  private def presetShape(radius: Float) : CircleShape = {
    val shape: CircleShape = new CircleShape
    shape.setRadius(radius)
    shape
  }

  private def presetFixture(filter: Filter, body: Body, shape: Shape): Fixture  = {
    val fixtureDefinition: FixtureDef = new FixtureDef()
    fixtureDefinition.shape = shape
    fixtureDefinition.density = density
    fixtureDefinition.filter = filter
    body.createFixture(fixtureDefinition)
  }

  override def receive: Receive = {
    case Destroyed =>
  }
}
