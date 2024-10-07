package ETL.infraestructure.readers

import ETL.domain.DataReader
import ETL.infraestructure.config.ConfigLoader.{ConnectionConfig, SourceConfig}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.log4j.Logger

/** SnowflakeReader implements the DataReader trait,
 * responsible for reading data from Snowflake.
 *
 * @param spark The Spark session used for reading data.
 * @param connectionConfig Configuration parameters for the connection.
 * @param sourceConfig Configuration parameters for the source.
 */
class SnowflakeReader(
                       spark: SparkSession,
                       connectionConfig: ConnectionConfig,
                       sourceConfig: SourceConfig
                     ) extends DataReader {

  private val logger = Logger.getLogger(getClass.getName)

  private val options: Map[String, String] = Map(
    "sfURL"       -> connectionConfig.sfURL,
    "sfUser"      -> connectionConfig.sfUser,
    "sfPassword"  -> connectionConfig.sfPassword,
    "sfWarehouse" -> connectionConfig.sfWarehouse,
    "sfRole"      -> connectionConfig.sfRole,
    "sfDatabase"  -> sourceConfig.sfDatabase,
    "sfSchema"    -> sourceConfig.sfSchema,
    "dbtable"     -> sourceConfig.dbtable
  )

  /** Reads data from Snowflake and returns it as a DataFrame.
   *
   * @return A DataFrame containing the read data.
   */
  override def readData(): DataFrame = {
    try {
      val data = spark.read
        .format("net.snowflake.spark.snowflake")
        .options(options)
        .load()
      logger.info("Data read successfully from Snowflake.")
      data
    } catch {
      case e: Exception =>
        logger.error("An error occurred while reading data from Snowflake:", e)
        throw e
    }
  }
}

// SOLID Principles Applied:
// - Liskov Substitution Principle (LSP): Can replace any implementation of DataReader.
// - Dependency Inversion Principle (DIP): Implements the DataReader interface from the domain layer.
// - Single Responsibility Principle (SRP): Responsible only for reading data from Snowflake.
