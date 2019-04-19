package com.heimdali.test.fixtures

import cats.effect.IO
import doobie.util.transactor.Transactor
import org.scalatest.{BeforeAndAfterEach, Suite}

import scala.concurrent.ExecutionContext

trait DBTest extends BeforeAndAfterEach { this: Suite =>

  implicit val contextShift = IO.contextShift(ExecutionContext.global)

  val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/heimdali",
    "postgres",
    "postgres"
  )
}
