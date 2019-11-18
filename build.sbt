name := "autosnom"

version := "0.1"

scalaVersion := "2.13.1"

enablePlugins(PlayService)
enablePlugins(RoutesCompiler)

libraryDependencies := Seq(
    guice,
    akkaHttpServer,
    logback,
)

scalacOptions ++= Seq("-unchecked", "-deprecation")