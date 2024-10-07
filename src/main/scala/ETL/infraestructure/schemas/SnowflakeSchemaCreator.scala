package ETL.infraestructure.schemas

import ETL.domain.SchemaCreator
import ETL.infraestructure.config.ConfigLoader.{ConnectionConfig, SinkConfig}
import net.snowflake.spark.snowflake.Utils
import org.apache.log4j.Logger

/** SnowflakeSchemaCreator implements the SchemaCreator trait,
 * responsible for creating databases and schemas in Snowflake.
 *
 * @param connectionConfig Configuration parameters for the connection.
 * @param sinkConfig Configuration parameters for the sink.
 */
class SnowflakeSchemaCreator(
                              connectionConfig: ConnectionConfig,
                              sinkConfig: SinkConfig
                            ) extends SchemaCreator {

  private val logger = Logger.getLogger(getClass.getName)

  private val options: Map[String, String] = Map(
    "sfURL"       -> connectionConfig.sfURL,
    "sfUser"      -> connectionConfig.sfUser,
    "sfPassword"  -> connectionConfig.sfPassword,
    "sfWarehouse" -> connectionConfig.sfWarehouse,
    "sfRole"      -> connectionConfig.sfRole
  )

  /** Creates the database and schema in Snowflake.
   * This method also creates the target table if it does not exist.
   */
  override def createDatabaseAndSchema(): Unit = {
    val database = sinkConfig.sfDatabase
    val schema   = sinkConfig.sfSchema
    val table    = sinkConfig.dbtable

    val updatedOptions = options ++ Map(
      "sfDatabase" -> database,
      "sfSchema"   -> schema
    )

    try {
      // Create database and schema if they do not exist
      executeQuery(s"CREATE DATABASE IF NOT EXISTS $database;", updatedOptions)
      executeQuery(s"CREATE SCHEMA IF NOT EXISTS $database.$schema;", updatedOptions)

      logger.info(s"Database $database and schema $schema are ready.")

      // Optional: Create table if it does not exist
      val createTableSQL =
        s"""
        CREATE TABLE IF NOT EXISTS $database.$schema.$table LIKE $database.$schema.$table;
        """

      executeQuery(createTableSQL, updatedOptions)
      logger.info(s"Table $table is ready in database $database and schema $schema.")

    } catch {
      case e: Exception =>
        logger.error("An error occurred during schema creation:", e)
        throw e
    }
  }

  private def executeQuery(sql: String, options: Map[String, String]): Unit = {
    Utils.runQuery(options, sql)
  }
}

// SOLID Principles Applied:
// - Liskov Substitution Principle (LSP): Can replace any implementation of SchemaCreator.
// - Dependency Inversion Principle (DIP): Implements the SchemaCreator interface from the domain layer.
// - Single Responsibility Principle (SRP): Responsible only for schema creation in Snowflake.
