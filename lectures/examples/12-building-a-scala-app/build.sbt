name := "building-a-scala-app"
version := "0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "cats-effect" % "3.3.12",
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC2",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC2",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC2",
  "com.typesafe" % "config" % "1.4.2",
  "co.fs2" %% "fs2-core" % "3.2.7",
  "co.fs2" %% "fs2-io" % "3.2.7",
  "co.fs2" %% "fs2-reactive-streams" % "3.2.7",
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1",
  "org.http4s" %% "http4s-dsl" % "0.23.11",
  "org.http4s" %% "http4s-blaze-server" % "0.23.11",
  "org.http4s" %% "http4s-blaze-client" % "0.23.11",
  "org.http4s" %% "http4s-circe" % "0.23.11",
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.typelevel" %% "cats-laws" % "2.7.0" % Test,
  "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test,
  "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test
)
