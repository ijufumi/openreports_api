package jp.ijufumi.openreports.controller.settings

import jp.ijufumi.openreports.controller.common.I18nFeature
import jp.ijufumi.openreports.service.settings.ReportSettingsService
import skinny.controller.feature.FileUploadFeature
import skinny.controller.{Params, SkinnyServlet}

class ReportSettingsController
    extends SkinnyServlet
    with FileUploadFeature
    with I18nFeature {

  val path = rootPath + "/report"
  val viewPath = rootPath + "/report"

  def index = {
    val reports = new ReportSettingsService().getReports()
    set("reports", reports)
    render(viewPath + "/index")
  }

  def showRegister = {
    render(viewPath + "/register")
  }

  def requestParams = Params(params)
}
