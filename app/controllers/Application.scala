package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import java.util.Date
import java.util.UUID

import anorm.NotAssigned
 
import models.{DeviceType, Device}
 
 
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
          Device.create(Device(NotAssigned, uuid, device_type_id, new Date, null))
          Redirect(routes.Application.index())
      }
    )
  }

}
