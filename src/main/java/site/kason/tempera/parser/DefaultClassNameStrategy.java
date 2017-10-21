package site.kason.tempera.parser;

import org.apache.commons.codec.digest.DigestUtils;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public class DefaultClassNameStrategy implements ClassNameStrategy {

  private final static String CLASS_NAME_PREFIX = "site.kason.tempera.generated.template.Tpl";

  @Override
  public String generateClassName(TemplateSource templateSource) {
    return CLASS_NAME_PREFIX + DigestUtils.md5Hex(templateSource.getContent());
  }

}
