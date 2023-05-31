package jp.ijufumi.openreports.gateways.datastores.database.repositories.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.entities.Workspace
import queries.{workspaceMemberQuery, workspaceQuery => query}
import jp.ijufumi.openreports.gateways.datastores.database.repositories.WorkspaceRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await

class WorkspaceRepositoryImpl @Inject() (db: Database) extends WorkspaceRepository {
  override def getById(id: String): Option[Workspace] = {
    val getById = query
      .filter(_.id === id)
    val workspaces = Await.result(db.run(getById.result), queryTimeout)
    if (workspaces.isEmpty) {
      return None
    }
    Some(workspaces.head)
  }

  override def getsByMemberId(memberId: String): Seq[Workspace] = {
    val getsByMemberId =
      query.join(workspaceMemberQuery).on(_.id === _.workspaceId).filter(_._2.memberId === memberId)
    val workspaces = Await.result(db.run(getsByMemberId.result), queryTimeout)
    workspaces.map(m => m._1)
  }

  override def register(workspace: Workspace): Option[Workspace] = {
    val register = (query += workspace).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(workspace.id)
  }

  override def update(workspace: Workspace): Option[Workspace] = {
    query.insertOrUpdate(workspace).withPinnedSession
    getById(workspace.id)
  }
}