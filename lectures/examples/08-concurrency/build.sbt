name := "concurrency"
version := "0.1"

scalaVersion := "3.1.1"

val akkaVersion = "2.6.19"

libraryDependencies ++= Seq(
  ("com.typesafe.akka" %% "akka-actor" % akkaVersion).cross(CrossVersion.for3Use2_13),
  ("com.typesafe.akka" %% "akka-actor-typed" % akkaVersion).cross(CrossVersion.for3Use2_13),
  ("com.typesafe.akka" %% "akka-stream" % akkaVersion).cross(CrossVersion.for3Use2_13),
  ("com.typesafe.akka" %% "akka-http" % "10.2.9").cross(CrossVersion.for3Use2_13),
  "org.asynchttpclient" % "async-http-client" % "2.12.3",
  "org.scalatest" %% "scalatest" % "3.2.11" % Test
)

scalacOptions ++= Seq(
  "-new-syntax",
  "-indent"
)
