package ETL.infraestructure.config

import com.typesafe.config.{Config, ConfigFactory}

/** ConfigLoader handles loading application configurations
 * from the application.conf file.
 */
object ConfigLoader {
  private val config: Config = ConfigFactory.load()

  /** Case class representing the connection configuration parameters. */
  case class ConnectionConfig(
                               sfURL: String,
                               sfUser: String,
                               sfPassword: String,
                               sfWarehouse: String,
                               sfRole: String
                             )

  /** Case class representing the source configuration parameters. */
  case class SourceConfig(
                           sfDatabase: String,
                           sfSchema: String,
                           dbtable: String
                         )

  /** Case class representing the sink configuration parameters. */
  case class SinkConfig(
                         sfDatabase: String,
                         sfSchema: String,
                         dbtable: String,
                         writeMode: String
                       )

  /** Case class representing the Spark configuration parameters. */
  case class SparkConfig(
                          master: String,
                          appName: String
                        )

  /** Loads connection configurations.
   *
   * @return A ConnectionConfig instance with the necessary parameters.
   */
  def loadConnectionConfig(): ConnectionConfig = {
    ConnectionConfig(
      sfURL = config.getString("connection.sfURL"),
      sfUser = config.getString("connection.sfUser"),
      sfPassword = sys.env.getOrElse("SF_PASSWORD", {
        throw new Exception("Environment variable SF_PASSWORD is not set")
      }),
      sfWarehouse = config.getString("connection.sfWarehouse"),
      sfRole = config.getString("connection.sfRole")
    )
  }

  /** Loads source configuration.
   *
   * @return A SourceConfig instance with the necessary parameters.
   */
  def loadSourceConfig(): SourceConfig = {
    SourceConfig(
      sfDatabase = config.getString("source.sfDatabase"),
      sfSchema = config.getString("source.sfSchema"),
      dbtable = config.getString("source.dbtable")
    )
  }

  /** Loads sink configuration.
   *
   * @return A SinkConfig instance with the necessary parameters.
   */
  def loadSinkConfig(): SinkConfig = {
    SinkConfig(
      sfDatabase = config.getString("sink.sfDatabase"),
      sfSchema = config.getString("sink.sfSchema"),
      dbtable = config.getString("sink.dbtable"),
      writeMode = config.getString("sink.writeMode")
    )
  }

  /** Loads Spark configuration.
   *
   * @return A SparkConfig instance with the necessary parameters.
   */
  def loadSparkConfig(): SparkConfig = {
    SparkConfig(
      master = config.getString("spark.master"),
      appName = config.getString("spark.appName")
    )
  }
}

// SOLID Principles Applied:
// - Single Responsibility Principle (SRP): Responsible only for loading configurations.
// - Open/Closed Principle (OCP): Can be extended without modifying existing code.
