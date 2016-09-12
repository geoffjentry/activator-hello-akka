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
  Cinnamon.library.cinnamonCHMetrics,
  Cinnamon.library.cinnamonAkka,
  "com.lightbend.cinnamon" %% "cinnamon-takipi" % "2.0.0"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")

lazy val app = project in file(".") enablePlugins (Cinnamon)
cinnamon in run := true
cinnamon in test := true

// Set the Monitoring Agent log level
cinnamonLogLevel := "DEBUG"

version := "0.0.1-SNAPSHOT"

fork in run := true
javaOptions in run += "-agentlib:TakipiAgent"