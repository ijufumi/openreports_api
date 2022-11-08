package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import jp.ijufumi.openreports.vo.request.UpdateReport

class ReportsServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {

  get("/") {
    val workspaceId = getWorkspaceId()
    val page = params("page").toInt
    val limit = params("limit").toInt
    okResult(reportService.getReports(workspaceId, page, limit))
  }

  post("/") {}

  get("/:id") {
    val workspaceId = getWorkspaceId()
    val id = params("id")
    val report = reportService.getReport(workspaceId, id)
    if (report.isEmpty) {
      notFoundResult("report not found")
    } else {
      okResult(report.get)
    }
  }

  get("/outputs/:id") {
    val workspaceId = getWorkspaceId()
    val id = params("id")
    val file = reportService.outputReport(workspaceId, id)
    if (file.isEmpty) {
      notFoundResult("report not found")
    } else {
      okResult(file.get)
    }
  }

  put("/:id") {
    val id = params("id")
    val workspaceId = getWorkspaceId()
    val requestParam = extractBody[UpdateReport]()
    val report =
      reportService.updateReport(workspaceId, id, requestParam.name, requestParam.reportTemplateId)
    if (report.isEmpty) {
      notFoundResult("Failed to update report")
    } else {
      okResult(report.get)
    }
  }

  delete("/:id") {}
}
