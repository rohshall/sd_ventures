package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json._

import java.util.Date
import java.util.UUID

import anorm.NotAssigned
 
import models._
 
 
object Application extends Controller {
 
  val deviceTypeForm = Form(
    tuple(
      "name" -> nonEmptyText,
      "version" -> nonEmptyText
    )
  )
 
  val deviceForm = Form(
    tuple(
      "uuid" -> nonEmptyText,
      "device_type_id" -> of[Long]
    )
  )
 
  def index = Action {
    val deviceTypes = DeviceType.findAll()
    val devices = Device.findAll()
    Ok(views.html.index( deviceTypeForm, deviceTypes, 
      deviceForm, devices ))
  }
 
  def addDeviceType() = Action { implicit request =>
    deviceTypeForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (name, version) =>
          DeviceType.create(DeviceType(NotAssigned, name, version))
          Redirect(routes.Application.index())
      }
    )
  }
  
  def addDevice() = Action { implicit request =>
    deviceForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (uuid_str, device_type_id) =>
          val uuid = UUID.fromString(uuid_str)
          Device.create(Device(NotAssigned, uuid, device_type_id, new Date, None))
          Redirect(routes.Application.index())
      }
    )
  }

  def getDevices() = Action { implicit request => {
    val devices = Device.findAll()
    val response = Json.toJson( devices.map { device =>
      Map("uuid" -> device.uuid.toString, 
        "device_type_id" -> device.device_type_id.toString,
        "manufactured_at" -> device.manufactured_at.toString)
    } )
    Ok( response )
  }
  }

  def addReading( device_uuid: String ) = Action( parse.json ) { implicit request => {
    (request.body \ "value").asOpt[String].map { value => {
      val reading = Reading(NotAssigned, UUID.fromString( device_uuid ),
        value, new Date)
      Reading.create( reading )
      Ok(Json.toJson(
        Map("status" -> "OK", "message" -> "Reading created!")
      ))
    }
    }.getOrElse {
      BadRequest("Missing parameter [value]")
    }
  }
  }


  def getReadings( device_uuid: String ) = Action { implicit request => {
    val readings = Reading.findAllForDevice( UUID.fromString( device_uuid ) )
    val response = Json.toJson( readings.map { reading =>
      Map("value" -> reading.value,
        "created_at" -> reading.created_at.toString)
    } )
    Ok( response )
  }
  }

}
