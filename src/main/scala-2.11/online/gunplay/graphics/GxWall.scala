import online.gunplay.graphics.{GxObject, GxStage}
import org.jbox2d.collision.shapes.{CircleShape, PolygonShape, Shape}
import org.jbox2d.dynamics.{BodyDef, BodyType, FixtureDef}

/**
  * Created by mike on 18.08.16.
  */

class GxWall(stage: GxStage, id: Long) extends GxObject {
  val body_definition: BodyDef = new BodyDef()
  body_definition.bullet = false
  body_definition.fixedRotation = true
  body_definition.`type` = BodyType.DYNAMIC
  body_definition.userData = this
  val body = stage.world.createBody(body_definition)
  val fixture_definition: FixtureDef = new FixtureDef
  val shape: Shape = new PolygonShape
  fixture_definition.shape = shape
  fixture_definition.density = 1.0f
  this.body.createFixture(fixture_definition)
}