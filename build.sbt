name := "WeeklyReports"

version := "1.0"

lazy val `weeklyreports` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( cache , ws   , specs2 % Test )

libraryDependencies += evolutions

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "be.objectify" %% "deadbolt-scala" % "2.4.3",
  "org.webjars" % "jTable" % "2.4.0",
  "com.h2database" % "h2" % "1.4.190",
  "org.webjars" % "bootstrap" % "3.1.1-2",
  "org.webjars" % "jquery-ui" % "1.11.4"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  