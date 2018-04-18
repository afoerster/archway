package com.heimdali.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.OAuth2BearerToken
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.heimdali.rest.ClusterController
import com.heimdali.services._
import com.heimdali.test.fixtures.FakeClusterService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Json
import io.circe.parser._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future

class ClusterControllerSpec
  extends FlatSpec
    with Matchers
    with ScalatestRouteTest
    with MockFactory
    with FailFastCirceSupport {

  behavior of "Cluster Controller"

  it should "get a list of clusters" in {
    val clusterService = mock[ClusterService]
    (clusterService.list _).expects().returning(Future(Seq(Cluster("cluster", "Odin", Map(
      "impala" -> HostClusterApp("Impala", "GOOD", "STARTED", "impala.example.com")
    ), CDH("5.11"), "GOOD_HEALTH"))))

    val Right(expected) = parse(
    """
      | [{
      |   "id": "cluster",
      |   "name": "Odin",
      |   "status": "GOOD_HEALTH",
      |   "services": {
      |     "impala": {
      |       "state": "STARTED",
      |       "status": "GOOD",
      |       "name": "Impala",
      |       "host": "impala.example.com"
      |     }
      |   },
      |   "distribution": {
      |     "name": "CDH",
      |     "version": "5.11"
      |   }
      | }]
    """.stripMargin
    )

    val restApi = new ClusterController(clusterService)

    Get("/clusters") ~> addCredentials(OAuth2BearerToken("AbCdEf123456")) ~> restApi.route ~> check {
      status should be(StatusCodes.OK)

      responseAs[Json] should be(expected)
    }
  }

}
