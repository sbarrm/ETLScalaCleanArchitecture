package ETL.infraestructure.loaders

import ETL.domain.DataLoader
import ETL.infraestructure.config.ConfigLoader.{ConnectionConfig, SinkConfig}
import org.apache.spark.sql.DataFrame
import org.apache.log4j.Logger

/** SnowflakeLoader implements the DataLoader trait,
 * responsible for loading data into Snowflake.
 *
 * @param connectionConfig Configuration parameters for the connection.
 * @param sinkConfig Configuration parameters for the sink.
 */
class SnowflakeLoader(
                       connectionConfig: ConnectionConfig,
                       sinkConfig: SinkConfig
                     ) extends DataLoader {

  private val logger = Logger.getLogger(getClass.getName)

  private val options: Map[String, String] = Map(
    "sfURL"       -> connectionConfig.sfURL,
    "sfUser"      -> connectionConfig.sfUser,
    "sfPassword"  -> connectionConfig.sfPassword,
    "sfWarehouse" -> connectionConfig.sfWarehouse,
    "sfRole"      -> connectionConfig.sfRole,
    "sfDatabase"  -> sinkConfig.sfDatabase,
    "sfSchema"    -> sinkConfig.sfSchema,
    "dbtable"     -> sinkConfig.dbtable
  )

  /** Loads the given data into Snowflake.
   *
   * @param data The DataFrame containing the data to load.
   */
  override def loadData(data: DataFrame): Unit = {
    try {
      data.write
        .format("net.snowflake.spark.snowflake")
        .options(options)
        .mode(sinkConfig.writeMode)
        .save()
      logger.info("Data loaded successfully into Snowflake.")
    } catch {
      case e: Exception =>
        logger.error("An error occurred while loading data into Snowflake:", e)
        throw e
    }
  }
}

// SOLID Principles Applied:
// - Liskov Substitution Principle (LSP): Can replace any implementation of DataLoader.
// - Dependency Inversion Principle (DIP): Implements the DataLoader interface from the domain layer.
// - Single Responsibility Principle (SRP): Responsible only for loading data into Snowflake.
