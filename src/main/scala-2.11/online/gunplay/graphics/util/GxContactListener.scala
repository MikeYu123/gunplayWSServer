package online.gunplay.graphics.util

import org.jbox2d.callbacks.{ContactImpulse, ContactListener}
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.contacts.Contact

/**
  * Created by mike on 18.08.16.
  */
class GxContactListener extends ContactListener {
  override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
    val bodies: (AnyRef, AnyRef) = contact.getFixtureA.getBody.getUserData -> contact.getFixtureB.getBody.getUserData
    bodies match {
      сase
    }

  }

  override def endContact(contact: Contact): Unit = {

  }

  override def beginContact(contact: Contact): Unit = {

  }

  override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {

  }
}
