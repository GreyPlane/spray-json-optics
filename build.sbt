ThisBuild / version := "0.1.1"

ThisBuild / scalaVersion := "2.13.8"

val monocleVersion = "3.1.0"
val sprayJsonVersion = "1.3.6"
val disciplineVersion = "1.5.1"

val publishSettings = List(
  organization := "io.github.greyplane",
  homepage := Some(url("https://github.com/GreyPlane/scribe-extension")),
  licenses := List(sbt.librarymanagement.License.MIT),
  developers := List(Developer("GreyPlane", "Liu Ji", "greyplane@gmail.com", url("https://github.com/GreyPlane"))),
  tlSonatypeUseLegacyHost := false)

inThisBuild(publishSettings)

lazy val sparyJsonOptics = (project in file(".")).settings(
  moduleName := "spray-json-optics",
  name := "spray-json-optics",
  libraryDependencies ++= Seq(
      "dev.optics" %% "monocle-core" % monocleVersion,
      "io.spray" %% "spray-json" % sprayJsonVersion,
      "dev.optics" %% "monocle-law" % monocleVersion,
      "org.scalatest" %% "scalatest" % "3.2.14" % Test,
      "org.scalacheck" %% "scalacheck" % "1.17.0" % Test,
      "org.typelevel" %% "discipline-core" % disciplineVersion % Test,
      "org.typelevel" %% "discipline-scalatest" % "2.2.0" % Test))
