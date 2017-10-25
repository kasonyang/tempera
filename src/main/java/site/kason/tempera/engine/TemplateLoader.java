package site.kason.tempera.engine;

/**
 *
 * @author Kason Yang
 */
public interface TemplateLoader {

  /**
   * load template source by name
   * @param templateName the template's name
   * @return the template source
   * @throws TemplateNotFoundException if template is not found
   */
  TemplateSource load(String templateName) throws TemplateNotFoundException;

}
