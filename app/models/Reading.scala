package models

import play.api.db._
import play.api.Play.current

import java.sql.Timestamp
 
import play.api.db.slick.Config.driver.simple._
 
case class Reading(id: Option[Int], device_mac_addr: String, value: String, created_at: Timestamp)
 
object Readings extends Table[Reading]("readings"){
 
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def device_mac_addr = column[String]("device_mac_addr", O.NotNull)
  def value = column[String]("value", O.NotNull)
  def created_at = column[Timestamp]("created_at")

  def * = id.? ~ device_mac_addr ~ value ~ created_at <> (Reading, Reading.unapply _)
  def forInsert = device_mac_addr ~ value ~ created_at <> (
    { t => Reading(None, t._1, t._2, t._3) }, 
    { (r: Reading) => Some((r.device_mac_addr, r.value, r.created_at)) })
 
  // A reified foreign key relation that can be navigated to create a join
  def device = foreignKey("device_fk", device_mac_addr, Devices)(_.mac_addr)
 
  lazy val database = Database.forDataSource(DB.getDataSource())
  
  def findAll(): Seq[Reading] = {
    database withSession { implicit session : Session =>
      Query(Readings).list
    }
  }
 
  def findAllForDevice(device_mac_addr: String): Seq[Reading] = {
    database withSession { implicit session : Session =>
      Query(Readings).filter(_.device_mac_addr === device_mac_addr).list
    }
  }
  
  def create(reading: Reading): Unit = {
    database withSession { implicit session : Session =>
      Readings.forInsert.insert(reading)
    }
  }
 
  def delete(reading: Reading): Unit = {
    database withSession { implicit session : Session =>
      val r = Query(Readings).filter(_.id === reading.id.get)
      r.delete
    }
  }
}
