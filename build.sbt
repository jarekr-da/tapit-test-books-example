val tapirVersion = "1.11.5"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "tapir-test",
    version := "0.1.0-SNAPSHOT",
    organization := "com.da.experiment",
    scalaVersion := "2.13.14",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-pekko-http-server" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "ch.qos.logback" % "logback-classic" % "1.5.8",
      "io.circe" %% "circe-generic-extras" % "0.14.4",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "com.softwaremill.sttp.client3" %% "circe" % "3.9.8" % Test
    )
  )
)
