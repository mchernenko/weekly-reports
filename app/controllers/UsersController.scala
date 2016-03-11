package controllers

import javax.inject.{Inject, Singleton}

import dao.UserDAO
import Users._
import models.Roles._
import play.api.mvc.{Action, Controller}
import play.api.libs.json._
import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.i18n._

@Singleton
class UsersController @Inject() (userDAO: UserDAO) extends Controller {

  def list(page: Int, pageSize: Int) = Action.async {
    Logger.info("List page=" + page + "; pageSize=" + pageSize)
    userDAO.list(page, pageSize).map(rows => Ok(Json.obj("Result" -> "OK", "Records" -> Json.toJson(rows))))
  }

  def create = Action.async { implicit request => {
      userForm.bindFromRequest().fold(
        formWithErrors => Future(Ok(Json.obj("Result" -> "Error", "Message" -> Json.toJson(
                formWithErrors.errors.map(e => {
                  "<b>" + Messages.get(e.key) + "</b>:<br>" +
                    e.messages.map("&nbsp;&nbsp;&nbsp;" + Messages.get(_)).mkString("<br>")
                }).mkString("<br>"))))),
        user => userDAO.insert(user).map(row => Ok(Json.obj("Result" -> "OK", "Record" -> Json.toJson(row))))
      )
    }
  }

  def update() = Action.async { implicit request => {
      userForm.bindFromRequest().fold(
        formWithErrors => Future(Ok(Json.obj("Result" -> "Error", "Message" -> Json.toJson(
                formWithErrors.errors.map(e => {
                  "<b>" + Messages.get(e.key) + "</b>:<br>" +
                    e.messages.map("&nbsp;&nbsp;&nbsp;" + Messages.get(_)).mkString("<br>")
                }).mkString("<br>")
              )
            )
          )
        ),
        user => userDAO.update(user).map(row => Ok(Json.obj("Result" -> "OK", "Record" -> Json.toJson(row))))
      )
    }
  }

  def delete = Action.async {
    implicit request => {
      val id = request.body.asFormUrlEncoded.get("id").headOption.map(_.toLong).getOrElse(0L)
      Logger.info("Delete id=" + id)
      userDAO.delete(id).map(_ => Ok(Json.obj("Result" -> "OK")))
    }
  }

  def roles = Action {
    Ok(Json.obj("Result" -> "OK", "Options" -> Json.toJson(RoleEnum.values.map(Json.toJson(_)))))
  }

}
