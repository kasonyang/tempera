package site.kason.tempera.engine;

/**
 *
 * @author Kason Yang
 */
public interface TemplateSource {

  /**
   * Get the name of template.
   * 
   * @return the name
   */
  String getName();

  /**
   * Get the content of template.
   * 
   * @return the content of template
   */
  String getContent();

  /**
   * Get the path of template. Just for the convenience of debugging.
   *
   * @return the path
   */
  String getPath();

  /**
   * Get the last modified time of template.
   * @return the timestamp
   */
  long lastModified();

}
