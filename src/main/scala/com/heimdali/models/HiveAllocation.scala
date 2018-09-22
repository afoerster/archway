package com.heimdali.models

import java.time.Instant

import cats._
import cats.effect.Effect
import cats.implicits._
import com.heimdali.tasks.ProvisionResult._
import com.heimdali.tasks.ProvisionTask._
import com.heimdali.tasks._
import io.circe._

import scala.concurrent.ExecutionContext

case class HiveAllocation(name: String,
                          location: String,
                          sizeInGB: Int,
                          consumedInGB: Double,
                          managingGroup: HiveGrant,
                          readonlyGroup: Option[HiveGrant] = None,
                          id: Option[Long] = None,
                          directoryCreated: Option[Instant] = None)

object HiveAllocation {

  def apply(name: String,
            location: String,
            sizeInGB: Int,
            consumedInGB: Double,
            managerLDAP: LDAPRegistration,
            readonlyLDAP: Option[LDAPRegistration]): HiveAllocation =
    apply(name, location, sizeInGB, consumedInGB, HiveGrant(name, location, managerLDAP), readonlyLDAP.map(ldap => HiveGrant(name, location, ldap)))

  implicit val viewer: Show[HiveAllocation] =
    Show.show(h => s"creating hive database ${h.name}")

  implicit def provisioner[F[_] : Effect](implicit executionContext: ExecutionContext): ProvisionTask[F, HiveAllocation] =
    ProvisionTask.instance { hive =>
      for {
        createDirectory <- CreateDatabaseDirectory(hive.id.get, hive.location, None).provision[F]
        setDiskQuota <- SetDiskQuota(hive.id.get, hive.location, hive.sizeInGB).provision[F]
        createDatabase <- CreateHiveDatabase(hive.id.get, hive.name, hive.location).provision[F]
        managers <- hive.managingGroup.provision[F]
      } yield createDirectory |+| setDiskQuota |+| createDatabase |+| managers
    }

  implicit val encoder: Encoder[HiveAllocation] =
    Encoder.forProduct7("id", "name", "location", "size_in_gb", "consumed_in_gb", "managing_group", "readonly_group")(s => (s.id, s.name, s.location, s.sizeInGB, s.consumedInGB, s.managingGroup, s.readonlyGroup))

  implicit final val decoder: Decoder[HiveAllocation] =
    Decoder.forProduct5("name", "location", "size_in_gb", "managing_group", "readonly_group")((name: String, location: String, size: Int, managing: HiveGrant, readonly: Option[HiveGrant]) =>
      HiveAllocation(name, location, size, 0, managing, readonly))

}