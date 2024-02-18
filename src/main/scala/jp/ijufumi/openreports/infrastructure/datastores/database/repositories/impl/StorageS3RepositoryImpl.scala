package jp.ijufumi.openreports.infrastructure.datastores.database.repositories.impl

import com.google.inject.Inject
import slick.jdbc.PostgresProfile.api._
import queries.{storageQuery => query}
import jp.ijufumi.openreports.infrastructure.datastores.database.repositories.StorageS3Repository
import jp.ijufumi.openreports.presentation.models.responses.StorageS3
import jp.ijufumi.openreports.utils.Dates
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await

class StorageS3RepositoryImpl @Inject() (db: Database) extends StorageS3Repository {
  override def getById(workspaceId: String, id: String): Option[StorageS3] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
      .filter(_.id === id)
    val models = Await.result(db.run(getById.result), queryTimeout)
    if (models.isEmpty) {
      return None
    }
    Some(models.head).map(s => StorageS3(s))
  }

  override def gets(workspaceId: String): Seq[StorageS3] = {
    val getById = query
      .filter(_.workspaceId === workspaceId)
    Await.result(db.run(getById.result), queryTimeout).map(s => StorageS3(s))
  }

  override def register(model: StorageS3): Option[StorageS3] = {
    val register = (query += model.toEntity).withPinnedSession
    Await.result(db.run(register), queryTimeout)
    getById(model.workspaceId, model.id)
  }

  override def update(model: StorageS3): Unit = {
    val newModel = model.copy(updatedAt = Dates.currentTimestamp())
    val updateQuery = query.insertOrUpdate(newModel.toEntity).withPinnedSession
    Await.result(db.run(updateQuery), queryTimeout)
  }
}