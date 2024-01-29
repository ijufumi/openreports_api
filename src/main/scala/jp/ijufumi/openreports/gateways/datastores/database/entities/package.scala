package jp.ijufumi.openreports.gateways.datastores.database

import jp.ijufumi.openreports.models.value.enums.JdbcDriverClasses.JdbcDriverClass
import jp.ijufumi.openreports.models.value.enums.RoleTypes.RoleType
import jp.ijufumi.openreports.models.value.enums.StorageTypes.StorageType
import slick.jdbc.JdbcType
import jp.ijufumi.openreports.models.value.enums.{
  ActionTypes,
  JdbcDriverClasses,
  RoleTypes,
  StorageTypes,
}
import slick.ast.BaseTypedType

package object entities {
  import slick.jdbc.PostgresProfile.api._

  abstract class EntityBase[T](tag: Tag, tableName: String) extends Table[T](tag, tableName) {
    def createdAt = column[Long]("created_at")
    def updatedAt = column[Long]("updated_at")
    def versions = column[Long]("versions")
  }

  implicit val jdbcDriverClassMapper
      : JdbcType[JdbcDriverClass] with BaseTypedType[JdbcDriverClass] =
    MappedColumnType.base[JdbcDriverClass, String](
      e => e.toString,
      s => JdbcDriverClasses.withName(s),
    )

  implicit val roleTypeMapper: JdbcType[RoleType] with BaseTypedType[RoleType] =
    MappedColumnType.base[RoleTypes.RoleType, String](
      e => e.toString,
      s => RoleTypes.withName(s),
    )

  implicit val storageTypeMapper: JdbcType[StorageType] with BaseTypedType[StorageType] =
    MappedColumnType.base[StorageTypes.StorageType, String](
      e => e.toString,
      s => StorageTypes.withName(s),
    )

  implicit val actionTypeMapper
      : JdbcType[ActionTypes.ActionType] with BaseTypedType[ActionTypes.ActionType] = {
    MappedColumnType.base[ActionTypes.ActionType, String](
      e => e.toString,
      s => ActionTypes.withName(s),
    )
  }
}
