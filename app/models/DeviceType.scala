package models
 
import play.api.db._
import play.api.Play.current
 
import anorm._
import anorm.SqlParser._
 
case class DeviceType(id: Pk[Long], name: String, version: String)
 
object DeviceType {
 
  val simple = {
    get[Pk[Long]]("id") ~
    get[String]("name") ~
    get[String]("version") map {
      case id ~ name ~ version => DeviceType(id, name, version)
    }
  }
 
  def findAll(): Seq[DeviceType] = {
    DB.withConnection { implicit connection =>
      SQL("select * from device_types").as(DeviceType.simple *)
    }
  }
 
  def create(device_type: DeviceType): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into device_types(name, version) values ({name}, {version})").on(
        'name -> device_type.name, 'version -> device_type.version
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
