name := "gameXback"

version := "1.0"

scalaVersion := "2.11.8"

ivyScala := ivyScala.value map {_.copy(overrideScalaVersion = true)}
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += DefaultMavenRepository

libraryDependencies ++= Seq(
  "org.jbox2d" % "jbox2d-library" % "2.2.1.1",
  "com.typesafe.akka" %% "akka-actor" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-agent" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-camel" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-cluster-metrics" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-contrib" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-http-core" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-http-testkit" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-osgi" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-persistence-tck" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-remote" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-stream" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-distributed-data-experimental" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-typed-experimental" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-http-jackson-experimental" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-http-xml-experimental" % "2.4.9-RC2",
  "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.9-RC2"
)