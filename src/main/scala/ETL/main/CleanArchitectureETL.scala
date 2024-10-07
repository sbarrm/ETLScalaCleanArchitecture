package ETL.main

import ETL.infraestructure.config.ConfigLoader
import ETL.infraestructure.loaders.SnowflakeLoader
import ETL.infraestructure.readers.SnowflakeReader
import ETL.infraestructure.schemas.SnowflakeSchemaCreator
import ETL.infraestructure.transformers.SparkDataTransformer
import ConfigLoader.{ConnectionConfig, SinkConfig, SourceConfig, SparkConfig}
import ETL.application.ETLJobUseCase
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.log4j.Logger

/** The entry point of the Clean Architecture ETL application.
 * This object initializes the Spark session, loads configurations,
 * and orchestrates the ETL process.
 */
object CleanArchitectureETL {
  def main(args: Array[String]): Unit = {
    // Set the log4j configuration file property before initializing the logger
    System.setProperty("log4j.configuration", "file:src/main/resources/log4j.properties")

    val logger = Logger.getLogger(getClass.getName)

    logger.info("Logging system initialized.")

    // Load Spark configurations
    val sparkConfig: SparkConfig = ConfigLoader.loadSparkConfig()

    val spark = SparkSession.builder()
      .appName(sparkConfig.appName)
      .master(sparkConfig.master)
      .getOrCreate()

    // Load other configurations
    val connectionConfig: ConnectionConfig = ConfigLoader.loadConnectionConfig()
    val sourceConfig: SourceConfig         = ConfigLoader.loadSourceConfig()
    val sinkConfig: SinkConfig             = ConfigLoader.loadSinkConfig()

    // Define the transformation logic
    val transformationLogic: DataFrame => DataFrame = data => {
      import org.apache.spark.sql.functions._
      data
        .filter(col("O_ORDERSTATUS") === "F")
        .withColumn("O_COMMENT_UPPER", upper(col("O_COMMENT")))
    }

    // Inject dependencies
    val schemaCreator = new SnowflakeSchemaCreator(connectionConfig, sinkConfig)
    val reader        = new SnowflakeReader(spark, connectionConfig, sourceConfig)
    val transformer   = new SparkDataTransformer(transformationLogic)
    val loader        = new SnowflakeLoader(connectionConfig, sinkConfig)

    // Execute the ETL job
    val etlJob = new ETLJobUseCase(schemaCreator, reader, transformer, loader)
    etlJob.execute()

    spark.stop()
    logger.info("ETL job completed successfully.")
  }
}

// SOLID Principles Applied:
// - Dependency Inversion Principle (DIP): High-level module (main application) depends on abstractions.
// - Single Responsibility Principle (SRP): Responsible only for setting up and running the ETL job.
