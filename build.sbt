name := "gameXback"

version := "1.0"

scalaVersion := "2.11.8"

ivyScala := ivyScala.value map {_.copy(overrideScalaVersion = true)}
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += DefaultMavenRepository

libraryDependencies ++= Seq(
  "org.jbox2d" % "jbox2d-library" % "2.2.1.1"
)