name := "library-app"
version := "0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "cats-effect" % "3.3.12",

  "org.http4s" %% "http4s-dsl" % "0.23.11",
  "org.http4s" %% "http4s-blaze-server" % "0.23.11",
  "org.http4s" %% "http4s-blaze-client" % "0.23.11",
  "org.http4s" %% "http4s-circe" % "0.23.11",

  "io.circe" %% "circe-generic" % "0.14.2",

  "ch.qos.logback" % "logback-classic" % "1.2.11",

  "org.scalatest" %% "scalatest" % "3.2.12" % Test
)
