package site.kason.tempera.engine;

import java.io.Writer;
import java.util.Map;

/**
 *
 * @author Kason Yang
 */
public interface Template {

  /**
   * Render the template.This method should be thread-safe.
   * @param data provided data
   * @param writer the writer for output
   */
  public void render(Map<String, Object> data, Writer writer);

  /**
   * Render the template.This method should be thread-safe.
   * @param data provided data
   * @return the result
   */
  public String render(Map<String, Object> data);

}
