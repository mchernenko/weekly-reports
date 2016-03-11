package models

import models.Roles.RoleEnum.Role
import models.Permissions.PermissionEnum
import play.api.data.format.Formatter
import play.api.data.{FormError, Forms, Mapping}
import play.api.libs.json.{Json, Writes}
import play.i18n.Messages

import be.objectify.deadbolt.core._

package object Roles {

  object RoleEnum extends Enumeration {
    val TeamLeader = new Role(Set(PermissionEnum.ReportLog, PermissionEnum.UserManagement, PermissionEnum.ReportGeneration))
    val Developer = new Role(Set(PermissionEnum.ReportLog))

    case class Role(permissions: Set[PermissionEnum.Permission]) extends Val with models.Role {
      override def getName: String = this.toString()
    }

    implicit def convert(value: Value) = value.asInstanceOf[Role]

    implicit def convert(role: Role) = role.toString()
  }

  implicit val roleWrites = new Writes[Role] {
    override def writes(role: Role) = Json.obj {
      "DisplayText" -> Messages.get(role.toString)
      "Value" -> role.toString
    }
  }

  val roleFormat: Formatter[Role] = new Formatter[Role] {
    override def unbind(key: String, value: Role): Map[String, String] = Map(key -> value.toString)

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Role] =
      play.api.data.format.Formats.stringFormat.bind(key, data).right.flatMap { s =>
        scala.util.control.Exception.allCatch[Role]
          .either(RoleEnum.withName(s).asInstanceOf[Role])
          .left.map(e => Seq(FormError(key, "error.enum", Nil)))
      }
  }

  val role: Mapping[Role] = Forms.of(roleFormat)
}
