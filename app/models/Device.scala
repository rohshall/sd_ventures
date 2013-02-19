package models
 
import play.api.db._
import play.api.Play.current
 
import java.sql.Timestamp
import java.util.UUID

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.session.Session


case class Device(id: Option[Int]= None, uuid: UUID, device_type_id: Int, manufactured_at: Timestamp, registered_at: Option[Timestamp])
 
object Devices extends Table[Device]("devices") {
  
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def uuid = column[UUID]("uuid")
  def device_type_id = column[Int]("device_type_id")
  def manufactured_at = column[Timestamp]("manufactured_at")
  def registered_at = column[Timestamp]("registered_at")

  def * = id.? ~ uuid ~ device_type_id ~ manufactured_at ~ registered_at.? <> (Device, Device.unapply _)
 
  lazy val database = Database.forDataSource(DB.getDataSource())
  
  def findAll(): Seq[Device] = {
    database withSession { implicit session : Session =>
      Query(Devices).list
    }
  }
 
  def create(device: Device): Unit = {
    database withSession { implicit session : Session =>
      Devices.insert(device)
    }
  }
 
  def delete(device: Device): Unit = {
    database withSession { implicit session : Session =>
      val d = Devices.filter(_.id == device.id)
      d.delete
    }
  }
}
