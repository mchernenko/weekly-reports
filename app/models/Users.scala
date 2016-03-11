import java.util

import _root_.models.Roles._
import _root_.models.Roles.RoleEnum.Role
import be.objectify.deadbolt.core
import be.objectify.deadbolt.core.models.{Permission, Subject}
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._

import collection.JavaConversions._

package object Users {

  case class User(id: Option[Long], firstName: String, lastName: String, email: String, role: Role, password: String) extends Subject {
    override def getRoles: util.List[_ <: core.models.Role] = List(role)
    override def getPermissions: util.List[_ <: Permission] = role.permissions.toList
    override def getIdentifier: String = id.toString
  }

  implicit val userWrites = new Writes[User] {
    override def writes(user: User) = Json.obj(
      "id" -> user.id,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "email" -> user.email,
      "role" -> user.role,
      "password" -> user.password
    )
  }

  val userForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> email,
      "role" -> role,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply)
  )
}
