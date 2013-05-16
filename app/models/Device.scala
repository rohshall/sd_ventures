package models

import play.api.db._
import play.api.Play.current
 
import java.sql.Timestamp

import play.api.db.slick.Config.driver.simple._


case class Device(id: Option[Int], mac_addr: String, device_type_id: Int, manufactured_at: Timestamp, registered_at: Option[Timestamp])
 
object Devices extends Table[Device]("devices") {
  
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def mac_addr = column[String]("mac_addr")
  def device_type_id = column[Int]("device_type_id")
  def manufactured_at = column[Timestamp]("manufactured_at")
  def registered_at = column[Option[Timestamp]]("registered_at")

  def * = id.? ~ mac_addr ~ device_type_id ~ manufactured_at ~ registered_at <> (Device, Device.unapply _)
  def forInsert = mac_addr ~ device_type_id ~ manufactured_at ~ registered_at <> (
    { t => Device(None, t._1, t._2, t._3, t._4) },
    { (d: Device) => Some((d.mac_addr, d.device_type_id, d.manufactured_at, d.registered_at)) })
 
  // A reified foreign key relation that can be navigated to create a join
  def deviceType = foreignKey("device_type_fk", device_type_id, DeviceTypes)(_.id)
  
  lazy val database = Database.forDataSource(DB.getDataSource())
  
  def findAll(): Seq[Device] = {
    database withSession { implicit session : Session =>
      Query(Devices).list
    }
  }
 
  def create(device: Device): Unit = {
    database withSession { implicit session : Session =>
      Devices.forInsert.insert(device)
    }
  }
 
  def delete(device: Device): Unit = {
    database withSession { implicit session : Session =>
      val d = Query(Devices).filter(_.id === device.id)
      d.delete
    }
  }
}
