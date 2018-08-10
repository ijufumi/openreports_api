package jp.ijufumi.openreports.service.enums

import scala.beans.BeanProperty
import scala.language.implicitConversions

case class ReportParamType(@BeanProperty paramId: String,
                           @BeanProperty paramName: String) {}

object ReportParamType extends Enumeration {

  val TEXT = Val("1", "Text")
  val DATE = Val("2", "Date")
  val LIST = Val("3", "List")
  val QUERY = Val("4", "Query")

  def of(value: String): ReportParamType.Val = {
    values.find(_.value == value).getOrElse(TEXT)
  }

  implicit def valueToVal(x: Value): Val = x.asInstanceOf[Val]

  protected case class Val(key: String, value: String) extends super.Val {
    def equals(value: String): Boolean = {
      Val.this == ReportParamType.of(key)
    }
  }

  def list(): Seq[ReportParamType] = {
    values.map(v => new ReportParamType(v.key, v.value))
  }
}
