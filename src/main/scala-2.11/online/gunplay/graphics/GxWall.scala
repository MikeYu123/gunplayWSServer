package online.gunplay.graphics
import online.gunplay.graphics.GxObject.ObjectData
import online.gunplay.graphics.{GxObject, GxStage}
import org.jbox2d.collision.shapes.{CircleShape, PolygonShape, Shape}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics._

/**
  * Created by mike on 18.08.16.
  */

object GxWall {
  val bullet: Boolean = false
  val bodyFixedRotation: Boolean = true
  val bodyType: BodyType = BodyType.STATIC
  val density: Float = 0.0f
  val groupIndex: Int = -2
  case class WallData(uuid: String) extends ObjectData(uuid)
}

class GxWall(override val stage: GxStage, override val uuid: String, sizes: RectSizes, override val position: Vec2)
  extends GxObject(stage, uuid) {
  import GxWall._

  override val body: Body = presetBody(position)
  val filter: Filter = presetFilter()
  val shape : Shape = presetShape(sizes)
  val fixture = presetFixture(filter, body, shape)

  private def presetBody(position: Vec2): Body = {
    val bodyDefinition: BodyDef = new BodyDef()
    bodyDefinition.bullet = bullet
    bodyDefinition.position = position
    bodyDefinition.fixedRotation = bodyFixedRotation
    bodyDefinition.`type` = bodyType
    bodyDefinition.userData = WallData(uuid)
    stage.world.createBody(bodyDefinition)
  }

  private def presetFilter() : Filter = {
    val filter = new Filter()
    filter.groupIndex = groupIndex
    filter
  }

  private def presetShape(rectSizes: RectSizes) : PolygonShape = {
    val shape: PolygonShape = new PolygonShape
    shape.setAsBox(sizes.width, sizes.height)
    shape
  }

  private def presetFixture(filter: Filter, body: Body, shape: Shape): Fixture  = {
    val fixtureDefinition: FixtureDef = new FixtureDef()
    fixtureDefinition.shape = shape
    fixtureDefinition.density = density
    fixtureDefinition.filter = filter
    body.createFixture(fixtureDefinition)
  }

  override def receive: Receive = ???
}