package online.gunplay.graphics

import org.jbox2d.collision.shapes.{CircleShape, PolygonShape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{BodyDef, BodyType, Filter, FixtureDef}

import scala.math.{cos, sin}
/**
  * Created by mike on 18.08.16.
  */
object GxBullet {
  val bulletOffset = 0.0055f
}

class GxBullet(override val stage: GxStage, override val id: Long, parent: GxObject) extends GxObject{
  import GxBullet._
  val parentRadius = parent.body.getFixtureList.getShape.getRadius
  val parentAngle = parent.body.getAngle
  val positionOffset = bulletOffset + parentRadius
  val posX = (positionOffset * cos(parentAngle)).toFloat
  val posY = (positionOffset * sin(parentAngle)).toFloat
  position = new Vec2(posX, posY)
  val body_definition: BodyDef = new BodyDef()
  body_definition.bullet = true
  body_definition.position = position
  body_definition.angle = parentAngle
//  body_definition.fixedRotation = false
  body_definition.`type` = BodyType.DYNAMIC
  body_definition.userData = this
  override val body = stage.world.createBody(body_definition)
  val fixture_definition: FixtureDef = new FixtureDef
  val shape: CircleShape = new CircleShape
  shape.setRadius(0.004f)
  fixture_definition.shape = shape
  fixture_definition.density = 0.0f
  val filter = new Filter()
  filter.groupIndex = -3
  fixture_definition.filter = filter
  this.body.createFixture(fixture_definition)
}
