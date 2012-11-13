package models
 
import play.api.db._
import play.api.Play.current

import java.util.Date
import java.util.UUID
 
import anorm._
import anorm.SqlParser._
 
case class Reading(id: Pk[Long], device_uuid: UUID, value: String, created_at: Date)
 
object Reading {
 
  implicit def rowToUuid: Column[UUID] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case d: UUID => Right(d)
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass + " to UUID for column " + qualified))
    }
  }
  
  val simple = {
    get[Pk[Long]]("id") ~
    get[UUID]("device_uuid") ~
    get[String]("value") ~
    get[Date]("created_at") map {
      case id ~ device_uuid ~ value ~ created_at => Reading(id, device_uuid, value, created_at)
    }
  }
 
  def findAll(): Seq[Reading] = {
    DB.withConnection { implicit connection =>
      SQL("select * from readings").as(Reading.simple *)
    }
  }
 
  def findDeviceReadings(device_uuid: UUID): Seq[Reading] = {
    DB.withConnection { implicit connection =>
      SQL("select * from readings where device_uuid = {device_uuid}").on(
        'device_uuid -> device_uuid
      ).as(Reading.simple *)
    }
  }
  def create(reading: Reading): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into readings(device_uuid, value, created_at) values ({device_uuid}, {value}, {created_at})").on(
        'device_uuid -> reading.device_uuid,
        'value -> reading.value, 
        'created_at -> reading.created_at
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
