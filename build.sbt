name := "hello-akka"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "takipi-sdk" at "https://dl.bintray.com/takipi/maven"

lazy val akkaVersion = "2.4.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
   "com.typesafe.cinnamon" %% "cinnamon-takipi" % "1.2.2"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
