package models
 
import play.api.db._
import play.api.Play.current
 
import anorm._
import anorm.SqlParser._
 
case class Device(id: Pk[Long], title: String)
 
object Device {
 
  val simple = {
    get[Pk[Long]]("id") ~
    get[String]("title") map {
      case id~name => Device(id, name)
    }
  }
 
  def findAll(): Seq[Device] = {
    DB.withConnection { implicit connection =>
      SQL("select * from devices").as(Device.simple *)
    }
  }
 
  def create(device: Device): Unit = {
    DB.withConnection { implicit connection =>
      SQL("insert into devices(title) values ({title})").on(
        'title -> device.title
      ).executeUpdate()
    }
  }
 
  def delete(device: Device): Unit = {
    DB.withConnection { implicit connection =>
      SQL("delete from devices where title = {title}").on(
        'title -> device.title
      ).executeUpdate()
    }
  }
}
