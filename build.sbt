ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "CleanArchitectureETL",
    Compile / doc / scalacOptions ++= Seq(
      "-doc-title", "CleanArchitectureETL",
      "-doc-version", version.value,
      "-diagrams",
      "-diagrams-dot-path", "C:\\Program Files\\Graphviz\\bin",
      "-diagrams-max-classes", "100",
      "-diagrams-max-implicits", "100",
      "-diagrams-dot-timeout", "60"
    )
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.3.0",
  "org.apache.spark" %% "spark-sql" % "3.3.0",
  "net.snowflake" %% "spark-snowflake" % "2.16.0-spark_3.3", // Updated version
  "com.typesafe" % "config" % "1.4.3",
  "org.apache.logging.log4j" % "log4j-api" % "2.23.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.23.1"
)


resolvers += "Maven Central" at "https://repo1.maven.org/maven2/"

// Uncomment if you want to enable UML Class Diagram Plugin
enablePlugins(UmlClassDiagramPlugin)




// Uncomment and configure if you want full diagram generation settings

classDiagramSettings := classDiagramSettings.value.copy(
  generateSvg = true,
  openFolder = true,
  openSvg = true,
  name = "diagrama_completo",
  enabledConnectionTypes =
    Set(
      com.leobenkel.umlclassdiagram.internal.ConnectionType.Inherit,
      com.leobenkel.umlclassdiagram.internal.ConnectionType.Produce,


    )
)
