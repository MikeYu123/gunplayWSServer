package online.gunplay.graphics

import org.jbox2d.collision.shapes.{CircleShape, Shape}
import org.jbox2d.dynamics.{BodyDef, BodyType, FixtureDef}

/**
  * Created by mike on 18.08.16.
  */
class GxPlayer(override val stage: GxStage, override val id: Long) extends GxObject{

  val player_body_definition: BodyDef = new BodyDef()
  player_body_definition.bullet = false
  player_body_definition.fixedRotation = true
  player_body_definition.`type` = BodyType.DYNAMIC
  player_body_definition.userData = this
  val body = stage.world.createBody(player_body_definition)
  val player_fixture_definition: FixtureDef = new FixtureDef
  val circle_shape: Shape = new CircleShape
  circle_shape.setRadius(0.2f)
  player_fixture_definition.shape = circle_shape
  player_fixture_definition.density = 1.0f
  this.body.createFixture(player_fixture_definition)
}
