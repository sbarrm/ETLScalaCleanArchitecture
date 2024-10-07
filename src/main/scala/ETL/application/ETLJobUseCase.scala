package ETL.application

import ETL.domain.{DataLoader, DataReader, DataTransformer, SchemaCreator}
import org.apache.log4j.Logger

/** ETLJobUseCase orchestrates the ETL process, handling
 * schema creation, data reading, transformation,
 * and loading processes.
 *
 * @param schemaCreator Responsible for creating the database and schema.
 * @param reader Responsible for reading data from Snowflake.
 * @param transformer Responsible for transforming the data.
 * @param loader Responsible for loading data into Snowflake.
 */
class ETLJobUseCase(
                     schemaCreator: SchemaCreator,
                     reader: DataReader,
                     transformer: DataTransformer,
                     loader: DataLoader
                   ) {
  private val logger = Logger.getLogger(getClass.getName)

  /** Executes the ETL process, handling all stages
   * from schema creation to data loading.
   */
  def execute(): Unit = {
    try {
      // Create database, schema, and table if they do not exist
      schemaCreator.createDatabaseAndSchema()

      // Read data from Snowflake
      val rawData = reader.readData()
      logger.info("Raw data read successfully.")

      // Transform data
      val transformedData = transformer.transform(rawData)
      logger.info("Data transformed successfully.")

      // Load data into Snowflake
      loader.loadData(transformedData)
      logger.info("Data loaded successfully into Snowflake.")
    } catch {
      case e: Exception =>
        logger.error("An error occurred during the ETL process:", e)
        throw e
    }
  }
}

// SOLID Principles Applied:
// - Single Responsibility Principle (SRP): This class is responsible only for orchestrating the ETL process.
// - Dependency Inversion Principle (DIP): Depends on abstractions (interfaces) rather than concrete implementations.
