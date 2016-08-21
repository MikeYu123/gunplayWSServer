package online.gunplay.graphics

import java.util.UUID

import akka.actor.{ActorRef, Props}
import online.gunplay.graphics.GxObject.ObjectData
import org.jbox2d.collision.shapes.{CircleShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics._
import scala.math._

/**
  * Created by mike on 18.08.16.
  */
object GxPlayer {
  val bulletOffset: Float = 0.0055f
  val bulletClass: Class[GxBullet] = classOf[GxBullet]
  val bullet: Boolean = false
  val bodyFixedRotation: Boolean = true
  val bodyType: BodyType = BodyType.DYNAMIC
  val radius: Float = 0.2f
  val density: Float = 1.0f
  val groupIndex: Int = 1
  val bulletSpeed: Float = 2.0f
  case class PlayerObjectData(uuid: String, name: String) extends ObjectData(uuid)
  case object HasKilled
  case object WasKilled
}

class GxPlayer(override val world: World, override val uuid: String, override val position: Vec2, val name: String)
  extends GxObject(world, uuid){
  import GxPlayer._

  override val body: Body = presetBody(position)
  val filter: Filter = presetFilter()
  val shape : Shape = presetShape(radius)
  val fixture = presetFixture(filter, body, shape)

  def emitBullet: ActorRef = {
    val selfRadius: Float = fixture.getShape.getRadius
    val selfAngle: Float = body.getAngle
    val selfCos: Double = cos(selfAngle)
    val selfSin: Double = sin(selfAngle)
    val bulletPositionOffset: Float = bulletOffset + selfRadius
    val bulletPosX: Float = (bulletPositionOffset * selfCos).toFloat
    val bulletPosY: Float = (bulletPositionOffset * selfSin).toFloat
    val bulletPosition: Vec2 = new Vec2(bulletPosX, bulletPosY)
    val velocity = new Vec2(selfCos, selfSin).mul(bulletSpeed)
    val uuid: String = UUID.randomUUID().toString
    context.actorOf(Props(bulletClass, uuid, bulletPosition, selfAngle, velocity))
  }

  private def presetBody(position: Vec2): Body = {
    val bodyDefinition: BodyDef = new BodyDef()
    bodyDefinition.bullet = bullet
    bodyDefinition.position = position
    bodyDefinition.fixedRotation = bodyFixedRotation
    bodyDefinition.`type` = bodyType
    bodyDefinition.userData = PlayerObjectData(uuid, name)
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
    case _ =>
  }
}
