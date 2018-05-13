package com.heimdali.test.fixtures

import org.scalatest.{BeforeAndAfterEach, Suite}
import scalikejdbc._

trait DBTest extends BeforeAndAfterEach { this: Suite =>
  def tables: Seq[SQLSyntaxSupport[_]]

  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.add('default, "jdbc:mysql://localhost:3306/heimdali", "root", "my-secret-pw")

  override protected def beforeEach(): Unit = {
    NamedDB('default) localTx { implicit session =>
      tables.foreach(table =>
        applyUpdate {
          delete.from(table)
        }
      )
    }
  }
}