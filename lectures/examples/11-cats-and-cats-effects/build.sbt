name := "cats-and-cats-effects"
version := "0.1"

scalaVersion := "3.1.1"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.typelevel" %% "cats-effect" % "3.3.11",
  "org.scalatest" %% "scalatest" % "3.2.11" % Test,
  "org.typelevel" %% "cats-laws" % "2.6.1" % Test,
  "org.typelevel" %% "discipline-scalatest" % "2.1.5" % Test,
  "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test
)
