package online.gunplay.graphics
import online.gunplay.graphics.{GxObject, GxStage}
import org.jbox2d.collision.shapes.{CircleShape, PolygonShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{BodyDef, BodyType, Filter, FixtureDef}

/**
  * Created by mike on 18.08.16.
  */
case class RectSizes(width: Float, height: Float)

class GxWall(override val stage: GxStage, override val id: Long, sizes: RectSizes)
  extends GxObject(stage, id) {
  val body_definition: BodyDef = new BodyDef()
  body_definition.bullet = false
  body_definition.position = position
//  body_definition.fixedRotation = true
  body_definition.`type` = BodyType.STATIC
  body_definition.userData = this
  override val body = stage.world.createBody(body_definition)
  val fixture_definition: FixtureDef = new FixtureDef
  val shape: PolygonShape = new PolygonShape
  shape.setAsBox(sizes.width, sizes.height)
  fixture_definition.shape = shape
  fixture_definition.density = 0.0f
  val filter = new Filter()
  filter.groupIndex = -2
  fixture_definition.filter = filter
  this.body.createFixture(fixture_definition)
}