package security

import javax.inject.{Inject, Singleton}

import be.objectify.deadbolt.scala.{DeadboltHandler, HandlerKey}
import be.objectify.deadbolt.scala.cache.HandlerCache
import dao.UserDAO

@Singleton
class DefaultHandlerCache @Inject() (userDAO: UserDAO) extends HandlerCache {

  private lazy val handler = new DefaultHandler(userDAO)

  override def apply(v: HandlerKey): DeadboltHandler = handler
  override def apply(): DeadboltHandler = handler
}
