
name := "taming-kittens"
version := "0.1.0"
scalacOptions += "-Ypartial-unification"
crossScalaVersions := Seq("2.12.10", "2.13.1")
resolvers += Resolver.sonatypeRepo("snapshots")

//Versions
val vCats = "2.1.1"
val vCirce = "0.13.0"
val vCirceEx = "0.13.0"
val vCirceConfig = "0.8.0"
val vHttp4s = "0.21.3"
val vLogback = "1.2.3"
val vFs2Kafka = "1.0.0"

// Dependencies
val catsEffects = "org.typelevel" %% "cats-core" % vCats
val circeGenerics = "io.circe" %% "circe-generic" % vCirce
val circeLiterals = "io.circe" %% "circe-literal" % vCirce
val circeGenericsExtras = "io.circe" %% "circe-generic-extras" % vCirceEx
val circeParser = "io.circe" %% "circe-parser" % vCirce
val circeConfig = "io.circe" %% "circe-config" % vCirceConfig
val http4sServer = "org.http4s" %% "http4s-blaze-server" % vHttp4s
val http4sCirce = "org.http4s" %% "http4s-circe" % vHttp4s
val htt4sDsl = "org.http4s" %% "http4s-dsl" % vHttp4s
val logback = "ch.qos.logback" % "logback-classic" % vLogback
val fs2Kafka = "com.github.fd4s" %% "fs2-kafka" % vFs2Kafka

libraryDependencies ++= Seq(
  catsEffects,
  circeGenerics,
  circeLiterals,
  circeGenericsExtras,
  circeParser,
  circeConfig,
  http4sServer,
  http4sCirce,
  htt4sDsl,
  logback,
  fs2Kafka
)

enablePlugins(ScalafmtPlugin)

// Note: This fixes error with sbt run not loading config properly
fork in run := true

