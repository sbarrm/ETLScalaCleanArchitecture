package ETL.infraestructure.transformers

import ETL.domain.DataTransformer
import org.apache.spark.sql.DataFrame

/** SparkDataTransformer implements the DataTransformer trait,
 * responsible for transforming data using a provided logic.
 *
 * @param transformationLogic The logic to transform the data.
 */
class SparkDataTransformer(transformationLogic: DataFrame => DataFrame) extends DataTransformer {
  /** Transforms the input data using the specified transformation logic.
   *
   * @param data The DataFrame containing the data to transform.
   * @return A DataFrame containing the transformed data.
   */
  override def transform(data: DataFrame): DataFrame = {
    transformationLogic(data)
  }
}

// SOLID Principles Applied:
// - Liskov Substitution Principle (LSP): Can replace any implementation of DataTransformer.
// - Dependency Inversion Principle (DIP): Implements the DataTransformer interface from the domain layer.
// - Open/Closed Principle (OCP): Behavior can be extended via the transformation logic without modifying the class.
