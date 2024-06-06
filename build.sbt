name := "autosnom"

version := "0.1"

scalaVersion := "3.4.2"

val proj = (project in file("."))
    .enablePlugins(PlayService)
    .enablePlugins(RoutesCompiler)

libraryDependencies ++= Seq(
    logback,
)

Compile / doc / sources := Seq.empty

jibRegistry := "ghcr.io"
jibOrganization := "pschichtel"
jibName := jibRegistry.value

jibBaseImage := "docker.io/library/eclipse-temurin:21-jre-alpine"

jibJvmFlags := "-Dplay.server.pidfile.path=/dev/null" :: Nil
