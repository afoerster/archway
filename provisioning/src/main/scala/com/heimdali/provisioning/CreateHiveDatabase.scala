package com.heimdali.provisioning

import cats._
import cats.data._
import cats.effect.Effect
import cats.implicits._
import com.heimdali.AppContext
import doobie.implicits._

case class CreateHiveDatabase(workspaceId: Long, name: String, location: String)

object CreateHiveDatabase {

  implicit val viewer: Show[CreateHiveDatabase] =
    Show.show(c => s"""creating Hive database "${c.name}" at "${c.location}"""")

  implicit def provisioner[F[_]](implicit F: Effect[F]): ProvisionTask[F, CreateHiveDatabase] =
    ProvisionTask.instance { create =>
      Kleisli[F, AppContext[F], ProvisionResult] { config =>
        config.hiveClient.createDatabase(create.name, create.location).attempt.flatMap {
          case Left(exception) => F.pure(Error(create, exception))
          case Right(_) =>
            F.map(config
              .databaseRepository
              .databaseCreated(create.workspaceId)
              .transact(config.transactor)) { _ => Success(create) }
        }
      }
    }

}