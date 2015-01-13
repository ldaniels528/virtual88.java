import sbt.Keys._
import sbt._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

assemblySettings

name := "javapc"

organization := "com.ldaniels528"

version := "0.431"

scalaVersion := "2.11.4"

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked",
  "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars")

mainClass in assembly := Some("org.ldaniels528.javapc.jbasic.app.BasicEmulator")

test in assembly := {}

// General Dependencies
libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "log4j" % "log4j" % "1.2.17"
  //"org.slf4j" % "slf4j-api" % "1.7.7",
  //"org.slf4j" % "slf4j-log4j12" % "1.7.7"
)

// Testing Dependencies
libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

// define the resolvers
resolvers ++= Seq(
  "Java Net" at "http://download.java.net/maven/2/",
  "Maven Central Server" at "http://repo1.maven.org/maven2",
  "Sonatype Repository" at "http://oss.sonatype.org/content/repositories/releases/",
  "Typesafe Releases Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe Snapshots Repository" at "http://repo.typesafe.com/typesafe/snapshots/"
)

resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
