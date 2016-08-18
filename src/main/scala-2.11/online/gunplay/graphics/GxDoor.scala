package online.gunplay.graphics

import akka.actor.Actor.Receive
import online.gunplay.graphics.GxDoor.DoorOrientation.DoorOrientation
import online.gunplay.graphics.GxDoor.DoorState.DoorState
import online.gunplay.graphics.GxObject.ObjectData
import org.jbox2d.collision.shapes.{PolygonShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.joints.{Joint, JointDef, RevoluteJoint, RevoluteJointDef}
import org.jbox2d.dynamics._

import scala.math.Pi

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
  case class DoorData(uuid: String) extends ObjectData(uuid)
  object DoorState extends Enumeration {
    type DoorState = Value
    val OpenedFront, Closed, OpenedBack = Value
  }
  object DoorOrientation extends Enumeration {
    type DoorOrientation = Value
    val Up, Right, Down, Left = Value
  }
  val doorMinAngle = (Pi * -0.5).toFloat
  val doorMaxAngle = (Pi * 0.5).toFloat
}

override class GxDoor(override val stage: GxStage, override val uuid: String, sizes: RectSizes, orientation: DoorOrientation, override val position: Vec2)
  extends GxObject(stage, uuid) {

  import GxDoor._

  var state: DoorState = DoorState.Closed
  val data: DoorData = DoorData(uuid)

  override val body: Body = presetBody(position)
  val filter: Filter = presetFilter()
  val shape: Shape = presetShape(sizes)
  val fixture: Fixture = presetFixture(filter, body, shape)
  val vertex: Body = presetVertex(position, sizes, orientation)
  val joint: Joint = presetJoint(body, vertex)

  private def presetBody(position: Vec2): Body = {
    val bodyDefinition: BodyDef = new BodyDef()
    bodyDefinition.bullet = bullet
    bodyDefinition.position = position
    bodyDefinition.fixedRotation = bodyFixedRotation
    bodyDefinition.`type` = bodyType
    bodyDefinition.userData = data
    stage.world.createBody(bodyDefinition)
  }

  private def presetVertex(centerPointPosition: Vec2, sizes: RectSizes, orientation: DoorOrientation) = {
    val vertexPosition: Vec2 = initVertexPosition(centerPointPosition, sizes, orientation)
    val vertexDefinition: BodyDef = new BodyDef()
    vertexDefinition.position = vertexPosition
    vertexDefinition.userData = data
    stage.world.createBody(vertexDefinition)
  }

  private def presetJoint(centerPoint: Body, vertex: Body): Joint = {
    val jointDefinition: RevoluteJointDef = new RevoluteJointDef()
    jointDefinition.bodyA = centerPoint
    jointDefinition.bodyB = vertex
    //  TODO: in python there is only anchor, no anchorA or anchorB
    jointDefinition.localAnchorB = vertex.getWorldCenter
    jointDefinition.lowerAngle = doorMinAngle
    jointDefinition.lowerAngle = doorMaxAngle
    stage.world.createJoint(jointDefinition)
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


  override def locked: Receive = {

  }

  override def receive: Receive = {
  }
}
