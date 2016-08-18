package online.gunplay.graphics

import org.jbox2d.collision.shapes.{CircleShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{BodyDef, BodyType, Filter, FixtureDef}

/**
  * Created by mike on 18.08.16.
  */
class GxPlayer(override val stage: GxStage, override val id: Long, override var position: Vec2) extends GxObject{
  val body_definition: BodyDef = new BodyDef()
  body_definition.bullet = false
  body_definition.fixedRotation = true
  body_definition.position = position
  body_definition.`type` = BodyType.DYNAMIC
  body_definition.userData = this
  val body = stage.world.createBody(body_definition)
  val fixture_definition: FixtureDef = new FixtureDef
  val shape: CircleShape = new CircleShape
  shape.setRadius(0.2f)
  fixture_definition.shape = shape
  fixture_definition.density = 1.0f
  val filter = new Filter()
  filter.groupIndex = 1
  fixture_definition.filter = filter
  this.body.createFixture(fixture_definition)

}
