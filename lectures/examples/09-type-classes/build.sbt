name := "type-classes"
version := "0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "spire" % "0.18.0-M3",
  "org.scalatest" %% "scalatest" % "3.2.11" % Test
)
