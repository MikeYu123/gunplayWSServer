package online.gunplay.graphics

import akka.actor.Actor.Receive
import online.gunplay.graphics.GxDoor.DoorOrientation.DoorOrientation
import online.gunplay.graphics.GxDoor.DoorState.DoorState
import GxObject.ObjectData
import online.gunplay.graphics.DoorOrientation.DoorOrientation
import online.gunplay.graphics.GxStage.Update
import org.jbox2d.collision.shapes.{PolygonShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.joints.{Joint, JointDef, RevoluteJoint, RevoluteJointDef}
import org.jbox2d.dynamics._

import scala.math.{Pi, abs}

/**
  * Created by mike on 18.08.16.
  */
//TODO orientation to case objects

object GxDoor {
  val bullet: Boolean = false
  val bodyFixedRotation: Boolean = false
  val bodyType: BodyType = BodyType.DYNAMIC
  val density: Float = 2.0f
  val groupIndex: Int = -2
  case class DoorObjectData(uuid: String) extends ObjectData(uuid)
  object DoorState extends Enumeration {
    type DoorState = Value
    val OpenedFront = Value(1)
    val Closed = Value(0)
    val OpenedBack = Value(-1)
  }
  val doorMinAngle = (Pi * -0.5).toFloat
  val doorMaxAngle = (Pi * 0.5).toFloat
  case class EndRotating(state: DoorState)
  case class StartRotating(manifoldPoint: Vec2)
}

class GxDoor(override val world: World, override val uuid: String, sizes: RectSizes, orientation: DoorOrientation, override val position: Vec2)
  extends GxObject(world, uuid) {

  import GxDoor._

  var state: DoorState = DoorState.Closed
  val data: DoorObjectData = DoorObjectData(uuid)

  override val body: Body = presetBody(position)
  val filter: Filter = presetFilter()
  val shape: Shape = presetShape(sizes)
  val fixture: Fixture = presetFixture(filter, body, shape)
  val vertex: Body = presetVertex(position, sizes, orientation)
  val joint: RevoluteJoint = presetJoint(body, vertex)

  private def presetBody(position: Vec2): Body = {
    val bodyDefinition: BodyDef = new BodyDef()
    bodyDefinition.bullet = bullet
    bodyDefinition.position = position
    bodyDefinition.fixedRotation = bodyFixedRotation
    bodyDefinition.`type` = bodyType
    bodyDefinition.userData = data
    world.createBody(bodyDefinition)
  }

  private def presetVertex(centerPointPosition: Vec2, sizes: RectSizes, orientation: DoorOrientation) = {
    val vertexPosition: Vec2 = initVertexPosition(centerPointPosition, sizes, orientation)
    val vertexDefinition: BodyDef = new BodyDef()
    vertexDefinition.position = vertexPosition
    vertexDefinition.userData = data
    world.createBody(vertexDefinition)
  }

  private def presetJoint(centerPoint: Body, vertex: Body): RevoluteJoint = {
    val jointDefinition: RevoluteJointDef = new RevoluteJointDef()
    jointDefinition.bodyA = centerPoint
    jointDefinition.bodyB = vertex
    //  TODO: in python there is only anchor, no anchorA or anchorB
    jointDefinition.localAnchorB = vertex.getWorldCenter
    jointDefinition.lowerAngle = doorMinAngle
    jointDefinition.lowerAngle = doorMaxAngle
    world.createJoint(jointDefinition).asInstanceOf[RevoluteJoint]
  }

  private def initVertexPosition(centerPointPosition: Vec2, sizes: RectSizes, orientation: DoorOrientation): Vec2 = {
    orientation match {
      case DoorOrientation.Right =>
        val pointX = centerPointPosition.x - sizes.width + sizes.height
        val pointY = centerPointPosition.y
        new Vec2(pointX, pointY)
      case DoorOrientation.Down =>
        val pointX = centerPointPosition.x
        val pointY = centerPointPosition.y + sizes.width - sizes.height
        new Vec2(pointX, pointY)
      case DoorOrientation.Left =>
        val pointX = centerPointPosition.x + sizes.width - sizes.height
        val pointY = centerPointPosition.y
        new Vec2(pointX, pointY)
      case DoorOrientation.Up =>
        val pointX = centerPointPosition.x
        val pointY = centerPointPosition.y - sizes.width + sizes.height
        new Vec2(pointX, pointY)
    }
  }

  private def presetFilter(): Filter = {
    val filter = new Filter()
    filter.groupIndex = groupIndex
    filter
  }

  private def presetShape(rectSizes: RectSizes): PolygonShape = {
    val shape: PolygonShape = new PolygonShape
    shape.setAsBox(sizes.width, sizes.height)
    shape
  }

  private def presetFixture(filter: Filter, body: Body, shape: Shape): Fixture = {
    val fixtureDefinition: FixtureDef = new FixtureDef()
    fixtureDefinition.shape = shape
    fixtureDefinition.density = density
    fixtureDefinition.filter = filter
    body.createFixture(fixtureDefinition)
  }


  def locked: Receive = {
    case Update =>
      val delta = abs(angle - body.getAngle)
      if (delta < 0.05) {
        joint.enableMotor(false)
        joint.setMotorSpeed(0)
        //        impossible in jbox2d :((
        //        body.setAngle(angle)
        body.setAngularVelocity(0)
        body.setLinearVelocity(new Vec2(0, 0))
        body.setFixedRotation(true)
        context.become(receive)
      }
  }

  override def receive: Receive = {
    case StartRotating(manifoldPoint) =>
      val newState = state match {
        case DoorState.Closed => direction(manifoldPoint)
        case _ => DoorState.Closed
      }
//    TODO: Configs???
      val d: Int = newState.id
      state = newState
      angle = (d * Pi / 2).toFloat
//      TODO Check if works, replace otherwise:
//      body.getJointList.joint.asInstanceOf[RevoluteJoint].enableMotor(true)
      joint.enableMotor(true)
//      body.getJointList.joint.asInstanceOf[RevoluteJoint].setMaxMotorTorque(10)
      joint.setMaxMotorTorque(10)
      body.setFixedRotation(false)
      //      body.getJointList.joint.asInstanceOf[RevoluteJoint].setMotorSpeed((-d * Pi).toFloat)
      joint.setMotorSpeed((-d * Pi).toFloat)
      context.become(locked)
  }

  private def direction(manifoldPoint: Vec2): DoorState = {
    val jointPoint: Vec2 = body.getJointList().other.getPosition()
    val doorPoint: Vec2 = body.getPosition()
    val a: Vec2 = doorPoint.sub(jointPoint)
    val b: Vec2 = manifoldPoint.sub(jointPoint)
    val p: Float = a.x * b.y - a.y * b.x
    p match {
      case 0.0f => DoorState.Closed
      case x if x < 0.0f => DoorState.OpenedFront
      case x if x > 0.0f => DoorState.OpenedBack
    }
  }
}
