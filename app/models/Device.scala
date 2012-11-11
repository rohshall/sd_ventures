package models
 
import play.api.db._
import play.api.Play.current
 
import java.util.Date

import anorm._
import anorm.SqlParser._
 
case class Device(id: Pk[Long], device_type_id: Long, registered_at: Date)
 
object Device {
 
  val simple = {
    get[Pk[Long]]("id") ~
    get[Long]("device_type_id") ~
    get[Date]("registered_at") map {
      case id~device_type_id~registered_at => Device(id, device_type_id, registered_at)
    }
  }
 
  def findAll(): Seq[Device] = {
    DB.withConnection { implicit connection =>
      SQL("select * from devices").as(Device.simple *)
    }
  }
 
  def create(device: Device): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into devices(device_type_id, registered_at) values ({device_type_id}, {registered_at})").on(
        'device_type_id -> device.device_type_id,
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
