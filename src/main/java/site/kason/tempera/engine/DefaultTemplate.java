package site.kason.tempera.engine;

import java.util.Map;
import site.kason.tempera.parser.TexTemplateBase;

/**
 *
 * @author Kason Yang
 */
public class DefaultTemplate implements Template {

  private Class<TexTemplateBase> renderClass;

  public DefaultTemplate(Class<TexTemplateBase> renderClass) {
    this.renderClass = renderClass;
  }

  @Override
  public String render(Map<String, Object> data) {
    try {
      TexTemplateBase tpl = renderClass.newInstance();
      return tpl.render(data);
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

}
