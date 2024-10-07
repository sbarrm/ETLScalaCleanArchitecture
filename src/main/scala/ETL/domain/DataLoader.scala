package ETL.domain

import org.apache.spark.sql.DataFrame

/** Trait defining a data loader.
 * It provides a method for loading data into a target system.
 */
trait DataLoader {
  /** Loads data into the target system.
   *
   * @param data The DataFrame containing the data to load.
   */
  def loadData(data: DataFrame): Unit
}

// SOLID Principles Applied:
// - Interface Segregation Principle (ISP): Provides a specific interface for loading data.
// - Dependency Inversion Principle (DIP): High-level modules can depend on this abstraction.
