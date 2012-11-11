package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import java.util.Date

import anorm.NotAssigned
 
import models.{DeviceType, Device}
 
 
object Application extends Controller {
 
  val deviceTypeForm = Form(
    single("dev_type" -> nonEmptyText)
  )
 
  val deviceForm = Form(
    single("device_type_id" -> of[Long])
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
        case (dev_type) =>
          DeviceType.create(DeviceType(NotAssigned, dev_type))
          Redirect(routes.Application.index())
      }
    )
  }
  
  def addDevice() = Action { implicit request =>
    deviceForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (device_type_id) =>
          Device.create(Device(NotAssigned, device_type_id, new Date))
          Redirect(routes.Application.index())
      }
    )
  }

}
