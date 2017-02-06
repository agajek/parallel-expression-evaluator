name := "expresion-evaluator"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "de.heikoseeberger" %% "akka-http-json4s" % "1.12.0",
  "com.typesafe.akka" %% "akka-http" % "10.0.3",
  "org.json4s" %% "json4s-jackson" % "3.5.0"
)
