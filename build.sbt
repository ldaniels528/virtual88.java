import sbt.Keys._
import sbt._

val scalaJvmVersion = "2.13.1"
val scalaTestVersion = "3.3.0-SNAP2"
val slf4jVersion = "1.7.30"

lazy val root = (project in file("."))
  .settings(
    name := "javapc",
    organization := "com.github.ldaniels528",
    description := "IBM PC/DOS Emulator",
    version := "0.431",
    scalaVersion := scalaJvmVersion,
    scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked", "-Ywarn-value-discard", "-Xlint"),
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars"),
    autoCompilerPlugins := true,
    mainClass in assembly := Some("org.ldaniels528.javapc.jbasic.app.BasicEmulator"),
    test in assembly := {},
    assemblyJarName in assembly := s"${name.value}-${version.value}.fat.jar",
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case PathList("org", "apache", xs @ _*) => MergeStrategy.last
      case "reference.conf" => MergeStrategy.concat
      case _ => MergeStrategy.first
    },
    libraryDependencies ++= Seq(
      // General Dependencies
      "commons-io" % "commons-io" % "2.4",
      "log4j" % "log4j" % "1.2.17",
      "org.slf4j" % "slf4j-api" % slf4jVersion,
      "org.slf4j" % "slf4j-log4j12" % slf4jVersion,

      // Testing Dependencies
      "junit" % "junit" % "4.11" % "test",
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    ))

