package jp.ijufumi.openreports.controller

import jp.ijufumi.openreports.controller.common.ApplicationController
import skinny.controller.feature.ThymeleafTemplateEngineFeature

class HomeController extends ApplicationController
    with ThymeleafTemplateEngineFeature {
  val path = privatePath + "/home"

  def index = {
    val memberInfo: Option[Any] = skinnySession.getAttribute("memberInfo")
    if (memberInfo.isDefined) {
      render("/home/index")
    } else {
      redirect(publicPath)
    }
  }

  def logout = {
    skinnySession.removeAttribute("memberInfo");
    redirect(publicPath)
  }
}
