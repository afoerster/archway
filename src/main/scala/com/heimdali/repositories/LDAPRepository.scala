package com.heimdali.repositories

import cats.data.OptionT
import com.heimdali.models.LDAPRegistration
import doobie.free.connection.ConnectionIO

trait LDAPRepository {

  def create(lDAPRegistration: LDAPRegistration): ConnectionIO[LDAPRegistration]

  def complete(id: Long): ConnectionIO[LDAPRegistration]

  def find[A <: DatabaseRole](workspaceId: Long, databaseName: String, databaseRole: A): OptionT[ConnectionIO, LDAPRegistration]

}

sealed trait DatabaseRole

object DatabaseRole {

  def unapply(role: String): Option[DatabaseRole] =
    role match {
      case _ if role matches "managers?" => Some(Manager)
      case "readonly" => Some(ReadOnly)
      case _ => None
    }
}

case object Manager extends DatabaseRole

case object ReadOnly extends DatabaseRole