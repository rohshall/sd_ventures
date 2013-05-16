import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "sd_ventures"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "postgresql" % "postgresql" % "9.1-901.jdbc4",
      "com.typesafe.play" %% "play-slick" % "0.3.2"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
