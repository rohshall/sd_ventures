import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "sd_ventures"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      jdbc,
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "com.typesafe" % "slick_2.10" % "1.0.0-RC2"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
