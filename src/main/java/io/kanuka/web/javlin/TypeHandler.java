package io.kanuka.web.javlin;

/**
 * Handles type conversion for path and query parameters.
 */
public interface TypeHandler {

  /**
   * Return the non-nullable type conversion method.
   */
  String asMethod();

  /**
   * Return the nullable type conversion method.
   */
  String toMethod();

  /**
   * The type for adding to imports.
   */
  String getImportType();

  /**
   * The short name.
   */
  String shortName();
}
