package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import com.codahale.jerkson.Json

import anorm.NotAssigned
 
import models.{DeviceType, Device}
 
 
object Application extends Controller {
 
  val deviceTypeForm = Form(
    single("dev_type" -> nonEmptyText)
  )
 
  def index = Action {
    val deviceTypes = DeviceType.findAll()
    Ok(views.html.index( deviceTypeForm, deviceTypes ))
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

  def listDeviceTypes() = Action {
    val deviceTypes = DeviceType.findAll()
    val json = Json.generate( deviceTypes )
    Ok(json).as("application/json")
  }
 
}
