import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "jPlayfulGPP"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
//      jdbc,
//      anorm,
      javaCore,
//      javaJdbc,
      javaEbean,
//     javaJpa,
//      filters,
      "org.webjars" % "webjars-play" % "2.1.0",
      "org.webjars" % "jquery" % "1.9.1",
      "org.webjars" % "bootstrap" % "2.3.0"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
