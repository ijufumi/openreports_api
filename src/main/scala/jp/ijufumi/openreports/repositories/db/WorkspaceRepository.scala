package jp.ijufumi.openreports.repositories.db

import jp.ijufumi.openreports.entities.Workspace
import slick.dbio.{DBIOAction, Effect, NoStream}

trait WorkspaceRepository {

  def getById(id: String): Option[Workspace]

  def register(workspace: Workspace): Option[Workspace]
}