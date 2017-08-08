package com.heimdali.controller

import com.heimdali.repositories.ProjectRepositoryImpl
import com.heimdali.services._
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import org.scalatestplus.play.{BaseOneAppPerSuite, FakeApplicationFactory}
import play.api.Application
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import slick.dbio.DBIOAction
import slick.jdbc.JdbcProfile

class ProjectControllerSpec
  extends FlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BaseOneAppPerSuite
    with FakeApplicationFactory {

  behavior of "ProjectController"

  it should "do something" in {
    val json = Json.obj(
      "name" -> "sesame",
      "purpose" -> "to do something cool"
    )

    val request = FakeRequest(POST, "/projects")
      .withHeaders(AUTHORIZATION -> "Bearer AbCdEf123456")
      .withHeaders(CONTENT_TYPE -> "application/json")
      .withJsonBody(json)

    val rootCall = route(app, request).get

    status(rootCall) should be(CREATED)

    val jsonResponse = contentAsJson(rootCall).as[JsObject]

    (jsonResponse \ "id").asOpt[Int] shouldBe defined
    (jsonResponse \ "name").as[String] should be("sesame")
    (jsonResponse \ "purpose").as[String] should be("to do something cool")

    val date = (jsonResponse \ "created").asOpt[DateTime]
    date shouldBe defined

    implicit val dateOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)
    date.get should be < DateTime.now

    val creator = (jsonResponse \ "created_by").asOpt[String]
    creator shouldBe defined
    creator.get shouldBe "username"
  }

  import play.api.inject.bind

  override val fakeApplication: Application =
    new GuiceApplicationBuilder()
      .overrides(bind[AccountService].to[PassiveAccountService])
      .build()

  override protected def beforeAll(): Unit = {
    val db = fakeApplication.injector.instanceOf[DatabaseConfigProvider].get.db
    val repo = fakeApplication.injector.instanceOf[ProjectRepositoryImpl]
    repo.createTable()
  }
}