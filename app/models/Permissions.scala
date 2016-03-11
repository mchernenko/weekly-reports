package models

import be.objectify.deadbolt.core._

package object Permissions {

  object PermissionEnum extends Enumeration {

    val UserManagement = new Permission("user-management")

    val ReportLog = new Permission("report-log")
    val ReportGeneration = new Permission("report-generation")

    case class Permission(name: String) extends Val(name) with models.Permission {
      override def getValue: String = this.toString()
    }

    implicit def convert(value: Value) = value.asInstanceOf[Permission]

    implicit def convert(permission: Permission) = permission.toString()
  }
}
