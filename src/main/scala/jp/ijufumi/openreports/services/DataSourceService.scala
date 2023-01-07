package jp.ijufumi.openreports.services

import jp.ijufumi.openreports.vo.request.{CreateDataSource, UpdateDataSource}

import java.sql.Connection
import jp.ijufumi.openreports.vo.response.DataSource

trait DataSourceService {
  def connection(workspaceId: String, dataSourceId: String): Connection

  def getDataSources(workspaceId: String): Seq[DataSource]

  def getDataSource(workspaceId: String, id: String): Option[DataSource]

  def registerDataSource(workspaceId: String, requestVal: CreateDataSource): Option[DataSource]

  def updateDataSource(workspaceId: String, id: String, updateDataSource: UpdateDataSource): Option[DataSource]

  def deleteDataSource(workspaceId: String, id: String): Unit
}
