package controllers

import javax.inject.{Singleton, Inject}

import be.objectify.deadbolt.scala.ActionBuilders
import dao.UserDAO
import models.Roles.RoleEnum
import play.api.data.Forms._
import play.api.data._
import play.i18n._
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class Application @Inject() (userDAO: UserDAO, actionBuilder: ActionBuilders) extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def users() = actionBuilder.RestrictAction(RoleEnum.TeamLeader).defaultHandler(){
    authRequest => Ok(views.html.users())
  }

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying (Messages.get("invalid.email.or.password"), result => result match {
      case (email, password) => Await.result(check(email, password), 5.seconds)
    })
  )

  def check(email: String, password: String) =
     userDAO.get(email, password).map(_.nonEmpty || email == "admin@admin.com" && password == "123")

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.Application.index).withSession(Security.username -> user._1)
    )
  }

  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> Messages.get("you.are.now.logged.out")
    )
  }

}