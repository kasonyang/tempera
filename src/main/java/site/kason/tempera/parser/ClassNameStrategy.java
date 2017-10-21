package site.kason.tempera.parser;

import javax.annotation.Nullable;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public interface ClassNameStrategy {

  public String generateClassName(TemplateSource templateSource);

}
