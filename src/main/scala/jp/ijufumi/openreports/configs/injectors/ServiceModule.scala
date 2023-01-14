package jp.ijufumi.openreports.configs.injectors

import jp.ijufumi.openreports.services.impl.{
  DataSourceServiceImpl,
  LoginServiceImpl,
  OutputServiceImpl,
  RoleServiceImpl,
  ReportServiceImpl,
  StorageServiceImpl,
  WorkspaceServiceImpl,
}
import jp.ijufumi.openreports.services.{
  DataSourceService,
  LoginService,
  OutputService,
  RoleService,
  ReportService,
  StorageService,
  WorkspaceService,
}

class ServiceModule extends BaseModule {
  override def configure(): Unit = {
    super.configure()
    bindAsSingleton(classOf[LoginService], classOf[LoginServiceImpl])
    bindAsSingleton(classOf[ReportService], classOf[ReportServiceImpl])
    bindAsSingleton(classOf[OutputService], classOf[OutputServiceImpl])
    bindAsSingleton(classOf[DataSourceService], classOf[DataSourceServiceImpl])
    bindAsSingleton(classOf[StorageService], classOf[StorageServiceImpl])
    bindAsSingleton(classOf[WorkspaceService], classOf[WorkspaceServiceImpl])
    bindAsSingleton(classOf[RoleService], classOf[RoleServiceImpl])
  }
}
