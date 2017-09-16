package site.kason.tempera.engine;

import java.io.StringWriter;
import java.io.Writer;
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
  public void render(Map<String, Object> data, Writer writer) {
    try {
      Renderer tpl = renderClass.newInstance();
      tpl.render(data, writer);
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public String render(Map<String, Object> data) {
    StringWriter writer = new StringWriter();
    render(data,writer);
    return writer.toString();
  }

}
