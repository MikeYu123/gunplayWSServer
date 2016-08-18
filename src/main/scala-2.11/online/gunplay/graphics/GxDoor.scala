package online.gunplay.graphics

import com.sun.java.swing.plaf.gtk.GTKConstants.Orientation
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.joints.{Joint, JointDef, RevoluteJoint, RevoluteJointDef}
import org.jbox2d.dynamics._

import scala.math.Pi

/**
  * Created by mike on 18.08.16.
  */
//TODO orientation to case objects
class GxDoor(override val stage: GxStage, override val id: Long, sizes: RectSizes, orientation: Int, override var position: Vec2) extends GxObject{
  val body_definition: BodyDef = new BodyDef()
  body_definition.bullet = false
  body_definition.position = position
//  body_definition.fixedRotation = true
  body_definition.`type` = BodyType.DYNAMIC
  body_definition.userData = this
  override val body = stage.world.createBody(body_definition)
  val fixture_definition: FixtureDef = new FixtureDef
  val shape: PolygonShape = new PolygonShape
  shape.setAsBox(sizes.width, sizes.height)
  fixture_definition.shape = shape
  fixture_definition.density = 2.0f
  val filter = new Filter()
  filter.groupIndex = -2
  fixture_definition.filter = filter
  this.body.createFixture(fixture_definition)
  val bodyPointPosition: Vec2 = orientation match {
    case 0 =>
      val pointX = position.x - sizes.width + sizes.height
      val pointY = position.y
      new Vec2(pointX, pointY)
    case 1 =>
      val pointX = position.x
      val pointY = position.y + sizes.width - sizes.height
      new Vec2(pointX, pointY)
    case 2 =>
      val pointX = position.x + sizes.width - sizes.height
      val pointY = position.y
      new Vec2(pointX, pointY)
    case 3 =>
      val pointX = position.x
      val pointY = position.y - sizes.width + sizes.height
      new Vec2(pointX, pointY)
  }
  val bodyPointDefinition: BodyDef = new BodyDef()
  bodyPointDefinition.position = bodyPointPosition
  bodyPointDefinition.userData = this
  val bodyPoint: Body = stage.world.createBody(bodyPointDefinition)
  val jointDefinition: RevoluteJointDef = new RevoluteJointDef()
  jointDefinition.bodyA = body
  jointDefinition.bodyB = bodyPoint
//  TODO: in python there is only anchor, no anchorA or anchorB
  jointDefinition.localAnchorB = bodyPoint.getWorldCenter
  jointDefinition.lowerAngle = (Pi * -0.5).toFloat
  jointDefinition.lowerAngle = (Pi * 0.5).toFloat
  val joint: Joint = stage.world.createJoint(jointDefinition)


}
