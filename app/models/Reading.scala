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
  def device_uuid = column[UUID]("device_uuid", O.NotNull)
  def value = column[String]("value")
  def created_at = column[Timestamp]("created_at")

  def * = id.? ~ device_uuid ~ value ~ created_at <> (Reading, Reading.unapply _)
  def forInsert = device_uuid ~ value ~ created_at <> (
    { t => Reading(None, t._1, t._2, t._3) }, 
    { (r: Reading) => Some((r.device_uuid, r.value, r.created_at)) })
 
  lazy val database = Database.forDataSource(DB.getDataSource())
  
  def findAll(): Seq[Reading] = {
    database withSession { implicit session : Session =>
      Query(Readings).list
    }
  }
 
  def findAllForDevice(device_uuid: UUID): Seq[Reading] = {
    database withSession { implicit session : Session =>
      Query(Readings).filter(_.device_uuid == device_uuid).list
    }
  }
  
  def create(reading: Reading): Unit = {
    database withSession { implicit session : Session =>
      Readings.forInsert.insert(reading)
    }
  }
 
  def delete(reading: Reading): Unit = {
    database withSession { implicit session : Session =>
      val r = Query(Readings).filter(_.id == reading.id.get)
      r.delete
    }
  }
}
