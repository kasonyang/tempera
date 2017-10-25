package site.kason.tempera.extension;

/**
 *
 * @author Kason Yang
 */
public interface Filter {

  /**
   * Filter value
   * @param value the value to filter
   * @return the value filtered.
   */
  String filter(Object value);

}
