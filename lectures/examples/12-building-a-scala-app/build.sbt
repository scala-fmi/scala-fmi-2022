name := "monads-and-applicatives"
version := "0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.7.0",

  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1"
)
