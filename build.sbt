lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """pass-bakery""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test",
  "org.typelevel" %% "cats-effect" % "3.3.14",
  "org.tpolecat" %% "doobie-core"      % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-postgres"  % "1.0.0-RC1",
  "org.tpolecat" %% "doobie-scalatest" % "1.0.0-RC1" % "test"
)
