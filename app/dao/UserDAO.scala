package dao

import javax.inject.{Singleton, Inject}

import Users.User
import models.Roles.RoleEnum
import models.Roles.RoleEnum.Role
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

trait UserTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  implicit lazy val myEnumMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => RoleEnum.withName(s).asInstanceOf[Role]
  )

  class Users(tag: Tag) extends Table[User](tag, "USER") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("FIRST_NAME")

    def lastName = column[String]("LAST_NAME")

    def email = column[String]("EMAIL")

    def role = column[Role]("ROLE")

    def password = column[String]("PASSWORD")

    def * = (id.?, firstName, lastName, email, role, password) <>(User.tupled, User.unapply _)
  }

}

@Singleton()
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserTable
with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  val users = TableQuery[Users]

  def count(): Future[Int] = {
    db.run(users.map(_.id).length.result)
  }

  def insert(user: User): Future[User] =
    db.run((users returning users.map(_.id) into ((user,id) => user.copy(id=Some(id)))) += user)

  def insert(users: Seq[User]): Future[Unit] =
    db.run(this.users ++= users).map(_ => ())

  def update(user: User): Future[User] = {
    val userToUpdate = user.copy(Some(user.id.get))
    db.run(users.filter(_.id === user.id).update(userToUpdate))
    Future(user)
  }

  def delete(id: Long): Future[Unit] =
    db.run(users.filter(_.id === id).delete).map(_ => ())

  def list(page: Int = 0, pageSize: Int = 10): Future[Seq[User]] = {
    val offset = pageSize * page
    val query = users.drop(offset).take(pageSize)
    db.run(query.result)
  }

  def get(email: String, password: String): Future[Option[User]] =
    db.run(users.filter(u => u.email === email && u.password === password).result.headOption)

  def get(email: String): Future[Option[User]] =
    db.run(users.filter(u => u.email === email).result.headOption)
}
