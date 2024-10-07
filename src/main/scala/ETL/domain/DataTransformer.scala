package ETL.domain

import org.apache.spark.sql.DataFrame

/** Trait defining a data transformer.
 * It provides a method for transforming data.
 */
trait DataTransformer {
  /** Transforms the input data and returns the transformed data.
   *
   * @param data The DataFrame containing the data to transform.
   * @return A DataFrame containing the transformed data.
   */
  def transform(data: DataFrame): DataFrame
}

// SOLID Principles Applied:
// - Interface Segregation Principle (ISP): Provides a specific interface for data transformation.
// - Dependency Inversion Principle (DIP): High-level modules can depend on this abstraction.
