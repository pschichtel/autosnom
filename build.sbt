name := "autosnom"

version := "0.1"

scalaVersion := "2.13.8"

maintainer := "Phillip Schichtel <phillip@schich.tel>"

val proj = (project in file("."))
    .enablePlugins(PlayService)
    .enablePlugins(RoutesCompiler)
    .enablePlugins(AshScriptPlugin)

libraryDependencies := Seq(
    guice,
    akkaHttpServer,
    logback,
)

scalacOptions ++= Seq("-unchecked", "-deprecation")

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

bashScriptTemplateLocation := proj.base / "src" / "main" / "resources" / "launch-script.sh"