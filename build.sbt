import sbt.Keys._

val gitHeadCommitSha = settingKey[Option[String]]("Determines the current git commit SHA")
gitHeadCommitSha := {
  try {
    Some(Process("git rev-parse HEAD").lines.head)
  } catch {
    case _: Exception => None
}
}

name := "akka2"

organization := "org.rg"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq("com.typesafe.akka" % "akka-actor_2.11" % "2.4.10",
                           "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5",
                            "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.4.0",
                             "ch.qos.logback" % "logback-classic" % "1.1.2"
)


version := "1.0-" + gitHeadCommitSha.value.getOrElse("SNAPSHOT")

