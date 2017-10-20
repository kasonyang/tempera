package site.kason.tempera.parser;

import javax.annotation.Nullable;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public interface ClassNameStrategy {

  public String generateClassName(String templateName,String content,@Nullable String cacheKey);

}
