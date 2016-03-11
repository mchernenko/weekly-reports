package security

import javax.inject.Inject

import be.objectify.deadbolt.core.models.Subject
import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import dao.UserDAO
import play.api.mvc.{Results, Result, Request}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class DefaultHandler(userDAO: UserDAO) extends DeadboltHandler {
  override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = Future(None)

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = Future(None)

  override def getSubject[A](request: Request[A]): Future[Option[Subject]] = request.session.get("email").map(userDAO.get(_)).getOrElse(Future(None))

  override def onAuthFailure[A](request: Request[A]): Future[Result] = Future(Results.Forbidden(views.html.forbidden()))
}
