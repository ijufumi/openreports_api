package jp.ijufumi.openreports.services.impl

import com.google.inject.Inject
import jp.ijufumi.openreports.gateways.datastores.database.entities.DataSource
import jp.ijufumi.openreports.exceptions.NotFoundException
import jp.ijufumi.openreports.gateways.datastores.database.repositories.DataSourceRepository
import jp.ijufumi.openreports.services.DataSourceService
import jp.ijufumi.openreports.models.inputs.{CreateDataSource, UpdateDataSource}
import jp.ijufumi.openreports.models.outputs.{DataSource => DataSourceResponse, Lists}
import jp.ijufumi.openreports.utils.IDs
import jp.ijufumi.openreports.gateways.datastores.database.pool.ConnectionPool

import java.sql.Connection

class DataSourceServiceImpl @Inject() (dataSourceRepository: DataSourceRepository)
    extends DataSourceService {
  def connection(workspaceId: String, dataSourceId: String): Connection = {
    val dataSourceOpt = dataSourceRepository.getByIdWithDriverType(workspaceId, dataSourceId)
    if (dataSourceOpt.isEmpty) {
      throw new NotFoundException()
    }
    val (dataSource, driverType) = dataSourceOpt.get
    ConnectionPool.newConnection(
      dataSource.name,
      dataSource.username,
      dataSource.password,
      dataSource.url,
      driverType.jdbcDriverClass,
    )
  }

  override def getDataSources(workspaceId: String): Lists[DataSourceResponse] = {
    val dataSources = dataSourceRepository.getAllWithDriverType(workspaceId)
    Lists(
      dataSources.map(d => DataSourceResponse(d._1, d._2)),
      0,
      dataSources.size,
      dataSources.size,
    )
  }

  override def getDataSource(workspaceId: String, id: String): Option[DataSourceResponse] = {
    val dataSource = dataSourceRepository.getByIdWithDriverType(workspaceId, id)
    dataSource.map(v => DataSourceResponse(v._1, v._2))
  }

  override def registerDataSource(
      workspaceId: String,
      requestVal: CreateDataSource,
  ): Option[DataSourceResponse] = {
    val dataSource = DataSource(
      IDs.ulid(),
      requestVal.name,
      requestVal.url,
      requestVal.username,
      requestVal.password,
      requestVal.driverTypeId,
      workspaceId,
    )
    dataSourceRepository.register(dataSource)
    getDataSource(workspaceId, dataSource.id)
  }

  override def updateDataSource(
      workspaceId: String,
      id: String,
      requestVal: UpdateDataSource,
  ): Option[DataSourceResponse] = {
    val dataSource = dataSourceRepository.getById(workspaceId, id)
    if (dataSource.isEmpty) {
      return None
    }
    val newDataSource = dataSource.get.copyForUpdate(requestVal)
    dataSourceRepository.update(newDataSource)
    getDataSource(workspaceId, newDataSource.id)
  }

  override def deleteDataSource(workspaceId: String, id: String): Unit = {
    dataSourceRepository.delete(workspaceId, id)
  }
}
