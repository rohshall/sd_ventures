package models

import play.api.db._
import play.api.Play.current
 
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.session.Session
 
case class DeviceType(id: Option[Int], name: String, version: String)
 
object DeviceTypes extends Table[DeviceType]("device_types") {
 
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def version = column[String]("version")

  def * = id.? ~ name ~ version <> (DeviceType, DeviceType.unapply _)
  def forInsert = name ~ version <> (
    { t => DeviceType(None, t._1, t._2) }, 
    { (dt: DeviceType) => Some((dt.name, dt.version)) })
 
  lazy val database = Database.forDataSource(DB.getDataSource())
  
  def findAll(): Seq[DeviceType] = {
    database withSession { implicit session : Session =>
      Query(DeviceTypes).list
    }
  }
 
  def create(device_type: DeviceType): Unit = {
    database withSession { implicit session : Session =>
      DeviceTypes.forInsert.insert(device_type)
    }
  }
 
  def delete(device_type: DeviceType): Unit = {
    database withSession { implicit session : Session =>
      val dt_query = Query(DeviceTypes).filter(_.id === device_type.id)
      dt_query.delete
    }
  }
}
