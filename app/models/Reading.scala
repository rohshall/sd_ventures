package models
 
import play.api.db._
import play.api.Play.current

import java.sql.Timestamp
import java.util.UUID
 
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.session.Session
 
case class Reading(id: Option[Int] = None, device_uuid: UUID, value: String, created_at: Timestamp)
 
object Readings extends Table[Reading]("readings"){
 
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def device_uuid = column[UUID]("device_uuid")
  def value = column[String]("value")
  def created_at = column[Timestamp]("created_at")

  def * = id.? ~ device_uuid ~ value ~ created_at <> (Reading, Reading.unapply _)
 
  def findAll(): Seq[Reading] = {
    database withSession { implicit session : Session =>
      Query(Readings).list
    }
  }
 
  lazy val database = Database.forDataSource(DB.getDataSource())
  
  def findAllForDevice(device_uuid: UUID): Seq[Reading] = {
    database withSession { implicit session : Session =>
      (for( r <- Readings if r.device_uuid == device_uuid) yield r).list
    }
  }
  
  def create(reading: Reading): Unit = {
    database withSession { implicit session : Session =>
      Readings.insert(reading)
    }
  }
 
  def delete(reading: Reading): Unit = {
    database withSession { implicit session : Session =>
      val r = Readings.filter(_.id == reading.id)
      r.delete
    }
  }
}
