package site.kason.tempera.engine;

import java.util.Map;
import site.kason.tempera.parser.Renderer;

/**
 *
 * @author Kason Yang
 */
public class DefaultTemplate implements Template {

  private Class<Renderer> renderClass;

  public DefaultTemplate(Class<Renderer> renderClass) {
    this.renderClass = renderClass;
  }

  @Override
  public String render(Map<String, Object> data) {
    try {
      Renderer tpl = renderClass.newInstance();
      return tpl.render(data);
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

}
