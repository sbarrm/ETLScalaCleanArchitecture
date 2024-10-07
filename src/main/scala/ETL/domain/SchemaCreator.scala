package ETL.domain

/** Trait defining a schema creator.
 * It provides a method for creating a database and schema.
 */
trait SchemaCreator {
  /** Creates the database and schema.
   * This method should handle the necessary operations
   * to ensure that the database and schema are present.
   */
  def createDatabaseAndSchema(): Unit
}

// SOLID Principles Applied:
// - Interface Segregation Principle (ISP): Provides a specific interface for schema creation.
// - Dependency Inversion Principle (DIP): High-level modules can depend on this abstraction.
