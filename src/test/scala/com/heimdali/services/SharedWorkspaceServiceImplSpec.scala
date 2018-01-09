package com.heimdali.services

import java.time.LocalDateTime
import java.util.concurrent.LinkedBlockingDeque

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.TestActor.Message
import akka.testkit.{TestActor, TestActorRef, TestProbe}
import com.heimdali.models.ViewModel.{HDFSProvision, SharedWorkspace, SharedWorkspaceRequest}
import com.heimdali.repositories.WorkspaceRepository
import com.heimdali.test.fixtures._
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.Future

class SharedWorkspaceServiceImplSpec extends AsyncFlatSpec with Matchers with AsyncMockFactory {

  behavior of "ProjectServiceImpl"

  it should "create a project" in {
    val probe = TestProbe()
    val repo = mock[WorkspaceRepository]
    val date = LocalDateTime.now
    val workspace = SharedWorkspaceRequest(TestProject.name, TestProject.purpose, TestProject.compliance, TestProject.hdfs, TestProject.yarn, None)
    repo.create _ expects workspace returning Future(TestProject())

    val projectServiceImpl = new WorkspaceServiceImpl(repo, factory)
    projectServiceImpl.create(workspace) map { newProject =>
      newProject should have(
        'id (123L),
        'name (workspace.name),
        'purpose (workspace.purpose),
        'compliance (workspace.compliance)
      )
    }
  }

  it should "list projects" in {
    val repo = mock[WorkspaceRepository]
    val Array(project1, _) = Array(
      TestProject(id = 123L),
      TestProject(id = 321L)
    )
    repo.list _ expects standardUsername returning Future {
      Seq(project1)
    }

    val projectServiceImpl = new WorkspaceServiceImpl(repo, factory)
    projectServiceImpl.list(standardUsername) map { projects =>
      projects.length should be(1)
      projects.head should be(project1)
    }
  }

  implicit val system: ActorSystem = ActorSystem()

  def factory(project: SharedWorkspace): ActorRef =
    TestActorRef.create(system, Props(classOf[TestActor], new LinkedBlockingDeque[Message]()))

}
