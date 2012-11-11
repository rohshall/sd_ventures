package models
 
import play.api.db._
import play.api.Play.current

import java.util.Date
 
import anorm._
import anorm.SqlParser._
 
case class Reading(id: Pk[Long], value: String, created_at: Date, device_id: Long)
 
object Reading {
 
  val simple = {
    get[Pk[Long]]("id") ~
    get[String]("value") ~
    get[Date]("created_at") ~
    get[Long]("device_id") map {
      case id~value~created_at~device_id => Reading(id, value, created_at, device_id)
    }
  }
 
  def findAll(): Seq[Reading] = {
    DB.withConnection { implicit connection =>
      SQL("select * from readings").as(Reading.simple *)
    }
  }
 
  def findDeviceReadings(device_id: Long): Seq[Reading] = {
    DB.withConnection { implicit connection =>
      SQL("select * from readings where device_id = {device_id}").on(
        'device_id -> device_id
      ).as(Reading.simple *)
    }
  }
  def create(reading: Reading): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into readings(value, created_at, device_id) values ({value}, {created_at}, {device_id})").on(
        'value -> reading.value, 'created_at -> reading.created_at, 'device_id -> reading.created_at
      ).executeUpdate()
    }
  }
 
  def delete(reading: Reading): Unit = {
    DB.withConnection { implicit connection =>
      SQL("delete from readings where id = {id}").on(
        'id -> reading.id
      ).executeUpdate()
    }
  }
}
