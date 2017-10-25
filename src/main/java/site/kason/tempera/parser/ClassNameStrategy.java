package site.kason.tempera.parser;

import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public interface ClassNameStrategy {

  /**
   * Create name for the template
   * @param templateSource the template source
   * @return the name
   */
  public String generateClassName(TemplateSource templateSource);

}
