package online.gunplay.graphics

import org.jbox2d.callbacks.{ContactImpulse, ContactListener}
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.contacts.Contact
import online.gunplay.graphics.GxObject.ObjectData
import online.gunplay.graphics.GxBullet.BulletObjectData
import online.gunplay.graphics.GxWall.WallObjectData
import online.gunplay.graphics.GxPlayer.PlayerObjectData
import online.gunplay.graphics.GxDoor.DoorObjectData
import GxBullet.Destroyed
import GxBullet.Killed
import GxPlayer.WasKilled
import GxDoor.StartRotating
import org.jbox2d.collision.WorldManifold

/**
  * Created by mike on 18.08.16.
  */
class GxContactListener(stage: GxStage) extends ContactListener {
  override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
  }

  override def endContact(contact: Contact): Unit = {

  }

  override def beginContact(contact: Contact): Unit = {

  }

  override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
    val bodies: (AnyRef, AnyRef) = contact.getFixtureA.getBody.getUserData -> contact.getFixtureB.getBody.getUserData
    bodies match {
      case (x: ObjectData, bullet: BulletObjectData) =>
        x match {
          case DoorObjectData(_) | WallObjectData(_) =>
            stage.children(bullet.uuid) ! Destroyed
          case PlayerObjectData(uuid, _) =>
            stage.children(bullet.uuid) ! Killed
            stage.children(uuid) ! WasKilled
        }
      case (player: PlayerObjectData, door: DoorObjectData) =>
        val manifold = new WorldManifold()
        contact.getWorldManifold(manifold)
        val manifoldPoint = manifold.points(0)
        stage.children(door.uuid) ! StartRotating(manifoldPoint)
    }
  }
}
