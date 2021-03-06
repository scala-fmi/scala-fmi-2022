package fmi.user

import cats.effect.IO
import fmi.infrastructure.db.DoobieDatabase.DbTransactor
import cats.syntax.functor.*
import doobie.*
import doobie.implicits.*
import doobie.postgres.sqlstate

object UserDbGivens:
  given Meta[UserRole] = Meta[String].imap(UserRole.valueOf)(_.toString)

class UsersDao(dbTransactor: DbTransactor):
  import UserDbGivens.given

  def retrieveUser(id: UserId): IO[Option[User]] =
    sql"""
      SELECT email, password_hash, role, name, age
      FROM "user"
      WHERE email = $id
    """
      .query[User]
      .option
      .transact(dbTransactor)

  def registerUser(user: User): IO[Either[UserAlreadyExists, User]] =
    sql"""
      INSERT INTO "user" (email, password_hash, role, name, age)
      VALUES (${user.id}, ${user.passwordHash}, ${user.role}, ${user.name}, ${user.age})
    """.update.run
      .as(user)
      .attemptSomeSqlState { case sqlstate.class23.UNIQUE_VIOLATION =>
        UserAlreadyExists(user.id)
      }
      .transact(dbTransactor)

  def deleteUser(id: UserId): IO[Unit] =
    sql"""
      DELETE FROM user
      WHERE email = $id
    """.update.run.void
      .transact(dbTransactor)
