package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import jp.ijufumi.openreports.service.ReportService
import jp.ijufumi.openreports.service.support.ReportingSupport
import jp.ijufumi.openreports.vo.{ApiResponse, MemberInfo}

import scala.collection.mutable

class ReportController extends ApplicationController {
  val path = privatePath + "/report"
  val viewPath = privatePath + "/report"

  override val activeMenu = "report"
  override val requiredMemberInfo = true

  def groupList = {
    val memberInfo: Option[MemberInfo] = skinnySession.getAttribute("memberInfo").asInstanceOf[Option[MemberInfo]]
    val reportGroups = ReportService().groupList(memberInfo.get.groups)
    set("reportGroups", reportGroups)
    render(viewPath + "/index")
  }

  def reportList = params.getAs[Long]("id").map { id =>
    val reports = ReportService().reportList(id)
    set("reports", reports)
    render(viewPath + "/report-list")
  } getOrElse haltWithBody(404)

  def download: Unit = {
    val fileStream = getClass.getClassLoader.getResourceAsStream("report/sample.xlsx")
    fileDownload(fileStream, "sample.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  }

  def prepareReport = params.getAs[Long]("id").map { id =>
    val pageNo = params.getAs[Int]("pageNo") getOrElse 0
    val (paramInfo, nextPageNo) = ReportService().paramInfo(id, pageNo)

    set("paramInfo", paramInfo)
    set("pageNo", pageNo)
    set("reportId", id)
    set("nextPageNo", nextPageNo)
    render(viewPath + "/prepare-report")
  } getOrElse haltWithBody(404)

  def setParams = params.getAs[Long]("reportId").map { id =>
    params.getAs[Int]("pageNo").map { pageNo =>
      val paramMap = updateParamMap(id, pageNo)

      skinnySession.setAttribute("paramMap", paramMap)
      status = 200
      toPrettyJSONString(ApiResponse("OK"))
    } getOrElse halt(status = 400, body = "pageNo is missing.")
  } getOrElse halt(status = 400, body = "reportId is missing.")


  def printOutReport: String = params.getAs[Long]("reportId").map { id =>
    params.getAs[Int]("pageNo").map { pageNo =>
      val paramMap = updateParamMap(id, pageNo)

      val templateFile = ReportService().report(id).map {
        _.templateFile
      } getOrElse ""

      val reportFileOpt = ReportingSupport().output(templateFile, paramMap.toMap)

      if (reportFileOpt.isDefined) {
        val reportFile = reportFileOpt.get

        fileDownload(reportFile.getAbsolutePath,
          reportFile.getName,
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
          false)
        //deleteFile(reportFile)
      }
      ""
    } getOrElse halt(status = 400, body = "pageNo is missing.")
  } getOrElse halt(status = 400, body = "reportId is missing.")

  def updateParamMap(id: Long, pageNo: Int): mutable.Map[String, String] = {
    val paramMap = skinnySession
      .getAs[mutable.Map[String, String]]("paramMap")
      .getOrElse(mutable.Map[String, String]())
    val (paramInfo, _) = ReportService().paramInfo(id, pageNo)
    for (key <- params.keys) {
      val requestedParam = paramInfo.find(_.paramKey == key)
      if (requestedParam.isDefined) {
        paramMap + key -> params.getAs[String](key)
      }
    }
    paramMap
  }

  //  def outputReport: Unit = {
  //    val reportFileOpt = ReportingSupportService("report/sample.xlsx").output()
  //    if (reportFileOpt.nonEmpty) {
  //      var reportFile = reportFileOpt.get
  //      fileDownload(reportFile.getAbsolutePath, reportFile.getName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  //      deleteFile(reportFile)
  //    }
  //  }
}
