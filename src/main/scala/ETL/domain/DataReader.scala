package ETL.domain

import org.apache.spark.sql.DataFrame

/** Trait defining a data reader.
 * It provides a method for reading data from a source.
 */
trait DataReader {
  /** Reads data from the source.
   *
   * @return A DataFrame containing the read data.
   */
  def readData(): DataFrame
}

// SOLID Principles Applied:
// - Interface Segregation Principle (ISP): Provides a specific interface for reading data.
// - Dependency Inversion Principle (DIP): High-level modules can depend on this abstraction.
