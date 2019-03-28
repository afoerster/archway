package com.heimdali.generators

import cats.effect.IO
import cats.implicits._
import com.heimdali.models._
import com.heimdali.services.ConfigService
import com.heimdali.test.fixtures._
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.prop._

import scala.collection.immutable._

class DefaultUserWorkspaceGeneratorSpec extends FlatSpec with MockFactory with Matchers {

  behavior of "DefaultUserWorkspaceGenerator"

  it should "user templates should generate workspaces" in {
    val configService = mock[ConfigService[IO]]
    configService.getAndSetNextGid _ expects () returning 123L.pure[IO] repeat 5 times()
    val ldapGenerator = new DefaultLDAPGroupGenerator[IO](appConfig, configService)
    val appGenerator = new DefaultApplicationGenerator[IO](appConfig, ldapGenerator)
    val topicGenerator = new DefaultTopicGenerator[IO](appConfig, ldapGenerator)
    val templateService = new DefaultUserWorkspaceGenerator[IO](appConfig, ldapGenerator, appGenerator, topicGenerator)
    val input = UserTemplate(standardUserDN, standardUsername, Some(1), Some(1), Some(1))
    val workspace = WorkspaceRequest(
      standardUsername,
      standardUsername,
      standardUsername,
      "user",
      standardUserDN,
      timer.instant,
      Compliance(phiData = false, pciData = false, piiData = false),
      singleUser = true,
      data = List(HiveAllocation(s"user_$standardUsername", s"/user/$standardUsername/db", 250, LDAPRegistration(s"cn=user_$standardUsername,ou=heimdali,dc=jotunn,dc=io", s"user_$standardUsername", s"role_user_$standardUsername", attributes = defaultLDAPAttributes(s"cn=user_$standardUsername,ou=heimdali,dc=jotunn,dc=io", s"user_$standardUsername")), None)),
      processing = List(Yarn(s"root.user.$standardUsername", 1, 1)))

    val expected = workspace.copy(
      kafkaTopics = List(topicGenerator.topicFor("default", 1, 1, workspace).unsafeRunSync())
    )

    val actual: WorkspaceRequest = templateService.workspaceFor(input).unsafeRunSync()
    actual.copy(requestDate = timer.instant) should be(expected.copy(requestDate = timer.instant))
  }

}
