name := "type-classes"
version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",
  "org.scala-lang" % "scala-reflect" % "2.13.8",
  "org.typelevel" %% "cats-core" % "2.7.0",
  "org.typelevel" %% "spire" % "0.17.0",
  "org.scalatest" %% "scalatest" % "3.2.11" % Test
)
