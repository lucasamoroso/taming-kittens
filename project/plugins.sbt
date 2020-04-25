// Makes our code tidy
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.4")

// Revolver allows us to use re-start and work a lot faster!
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// Native Packager allows us to create standalone jar
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.0")
