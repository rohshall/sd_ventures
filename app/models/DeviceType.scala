package models
 
import play.api.db._
import play.api.Play.current
 
import anorm._
import anorm.SqlParser._
 
case class DeviceType(id: Pk[Long], dev_type: String)
 
object DeviceType {
 
  val simple = {
    get[Pk[Long]]("id") ~
    get[String]("dev_type") map {
      case id~dev_type => DeviceType(id, dev_type)
    }
  }
 
  def findAll(): Seq[DeviceType] = {
    DB.withConnection { implicit connection =>
      SQL("select * from device_types").as(DeviceType.simple *)
    }
  }
 
  def create(device_type: DeviceType): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into device_types(dev_type) values ({dev_type})").on(
        'dev_type -> device_type.dev_type
      ).executeUpdate()
    }
  }
 
  def delete(device_type: DeviceType): Unit = {
    DB.withConnection { implicit connection =>
      SQL("delete from device_types where id = {id}").on(
        'id -> device_type.id
      ).executeUpdate()
    }
  }
}
