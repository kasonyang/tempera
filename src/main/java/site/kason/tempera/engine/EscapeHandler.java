package site.kason.tempera.engine;
/**
 *
 * @author Kason Yang
 */
public interface EscapeHandler {
  
  /**
   * Escape string
   * @param value the string to escape
   * @return the escaped string
   */
  String escape(String value);

}
