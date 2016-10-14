name := "hello-akka"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "takipi-sdk" at "https://dl.bintray.com/takipi/maven"

lazy val akkaVersion = "2.4.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "com.lightbend.cinnamon" %% "cinnamon-takipi" % "2.0.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.9",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.9"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

version := "0.0.1-SNAPSHOT"

fork in run := true
javaOptions in run += "-agentlib:TakipiAgent"