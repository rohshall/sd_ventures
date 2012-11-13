package models
 
import play.api.db._
import play.api.Play.current
 
import java.util.Date
import java.util.UUID

import anorm._
import anorm.SqlParser._
 
case class Device(id: Pk[Long], uuid: UUID, device_type_id: Long, manufactured_at: Date, registered_at: Option[Date])
 
object Device {
  
  implicit def rowToUuid: Column[UUID] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, nullable, clazz) = meta
    value match {
      case d: UUID => Right(d)
      case _ => Left(TypeDoesNotMatch("Cannot convert " + value + ":" + value.asInstanceOf[AnyRef].getClass + " to UUID for column " + qualified))
    }
  }
  
  val simple = {
    get[Pk[Long]]("id") ~
    get[UUID]("uuid") ~
    get[Long]("device_type_id") ~
    get[Date]("manufactured_at") ~
    get[Option[Date]]("registered_at") map {
      case id ~ uuid ~ device_type_id ~ manufactured_at ~ registered_at => Device(id, uuid, device_type_id, manufactured_at, registered_at)
    }
  }
 
  def findAll(): Seq[Device] = {
    DB.withConnection { implicit connection =>
      SQL("select * from devices").as(Device.simple *)
    }
  }
 
  def create(device: Device): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into devices(uuid, device_type_id, manufactured_at) values ({uuid}, {device_type_id}, {manufactured_at})").on(
        'uuid -> device.uuid,
        'device_type_id -> device.device_type_id,
        'manufactured_at -> device.manufactured_at,
        'registered_at -> device.registered_at
      ).executeUpdate()
    }
  }
 
  def delete(device: Device): Unit = {
    DB.withConnection { implicit connection =>
      SQL("delete from devices where id = {id}").on(
        'id -> device.id
      ).executeUpdate()
    }
  }
}
