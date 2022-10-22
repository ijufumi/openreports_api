package jp.ijufumi.openreports.api.private_

import com.google.inject.Inject
import jp.ijufumi.openreports.api.base.PrivateAPIServletBase
import jp.ijufumi.openreports.services.{LoginService, ReportService}
import org.scalatra.{NotFound, Ok}

class ReportsServlet @Inject() (loginService: LoginService, reportService: ReportService)
    extends PrivateAPIServletBase(loginService) {

  get("/:workspaceId") {
    val workspaceId = params("workspaceId")
    val page = params("page").toInt
    val limit = params("limit").toInt
    hookResult(Ok(reportService.getReports(workspaceId, page, limit)))
  }

  post("/") {}

  get("/:workspaceId/:id") {
    val workspaceId = params("workspaceId")
    val id = params("id")
    val report = reportService.getReport(workspaceId, id)
    if (report.isEmpty) {
      hookResult(NotFound("report not found"))
    } else {
      hookResult(Ok(report.get))
    }
  }

  get("/:workspaceId/output/:id") {
    val workspaceId = params("workspaceId")
    val id = params("id")
    val file = reportService.outputReport(workspaceId, id)
    if (file.isEmpty) {
      hookResult(NotFound("report not found"))
    } else {
      hookResult(Ok(file.get))
    }
  }

  put("/:id") {}

  delete("/:id") {}
}
