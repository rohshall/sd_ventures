package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json._

import java.util.Date
import java.sql.Timestamp

import models._
 
 
object Application extends Controller {
 
  val deviceTypeForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "version" -> nonEmptyText
    )
    ((name, version) => DeviceType(None, name, version))
    ((dt: DeviceType) => Some(dt.name, dt.version))
  )
 
  val deviceForm = Form(
    mapping(
      "mac_addr" -> nonEmptyText,
      "device_type_id" -> number
    )
    ((mac_addr, device_type_id) => {
        val date = new Date
        val timestamp = new Timestamp(date.getTime)
        Device(None, mac_addr, device_type_id, timestamp, None)
    })
    ((d: Device) => Some(d.mac_addr, d.device_type_id))
  )
 
  def index = Action {
    val deviceTypes = DeviceTypes.findAll()
    val devices = Devices.findAll()
    Ok(views.html.index( deviceTypeForm, deviceTypes, deviceForm, devices ))
  }
 
  def addDeviceType() = Action { implicit request =>
    deviceTypeForm.bindFromRequest.fold(
      errors => BadRequest,
      device_type => {
        DeviceTypes.create( device_type )
        Redirect(routes.Application.index())
      }
    )
  }
  
  def addDevice() = Action { implicit request =>
    deviceForm.bindFromRequest.fold(
      errors => BadRequest,
      device => {
        Devices.create( device )
        Redirect(routes.Application.index())
      }
    )
  }

  def getDevices() = Action { implicit request => {
      val devices = Devices.findAll()
      val response = Json.arr( devices.map { device =>
        Json.obj("mac_addr" -> device.mac_addr, 
          "device_type_id" -> device.device_type_id.toString,
          "manufactured_at" -> device.manufactured_at.toString)
      })
      Ok( response )
    }
  }

  def addReading( device_mac_addr: String ) = Action( parse.json ) { implicit request => {
      (request.body \ "value").validate[String].fold(
        errors => BadRequest("Missing parameter [value]"),
        value => {
          val date = new Date
          val timestamp = new Timestamp(date.getTime)
          val reading = Reading(None, device_mac_addr, value, timestamp)
          Readings.create( reading )
          Ok(Json.obj("status" -> "OK"))
        }
      )
    }
  }


  def getReadings( device_mac_addr: String ) = Action { implicit request => {
      val readings = Readings.findAllForDevice( device_mac_addr )
      val response = Json.arr( readings.map { reading =>
        Json.obj("value" -> reading.value, "created_at" -> reading.created_at.toString)
      } )
      Ok( response )
    }
  }

}
