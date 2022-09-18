ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val monocleVersion = "3.1.0"
val sprayJsonVersion = "1.3.6"
val disciplineVersion = "1.5.1"

lazy val root = (project in file(".")).settings(
  name := "spray-json-optics",
  libraryDependencies ++= Seq(
      "dev.optics" %% "monocle-core" % monocleVersion,
      "io.spray" %% "spray-json" % sprayJsonVersion,
      "dev.optics" %% "monocle-law" % monocleVersion,
      "org.scalatest" %% "scalatest" % "3.2.12" % Test,
      "org.scalacheck" %% "scalacheck" % "1.16.0" % Test,
      "org.typelevel" %% "discipline-core" % "1.5.1" % Test,
      "org.typelevel" %% "discipline-scalatest" % "2.2.0" % Test))
