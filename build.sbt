name := """test"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies += javaJdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
  
scalaVersion := "2.12.4"

libraryDependencies += guice
