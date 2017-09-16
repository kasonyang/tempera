package site.kason.tempera.engine;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import site.kason.tempera.extension.Function;
import site.kason.tempera.parser.Renderer;

/**
 *
 * @author Kason Yang
 */
public class DefaultTemplate implements Template {

  private Class<Renderer> renderClass;
  private final Map<String,Function> functions;

  public DefaultTemplate(Class<Renderer> renderClass, Map<String,Function> functions) {
    this.renderClass = renderClass;
    this.functions = functions;
  }

  @Override
  public void render(Map<String, Object> data, Writer writer) {
    try {
      Renderer tpl = renderClass.newInstance();
      tpl.render(data, writer, functions);
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public String render(Map<String, Object> data) {
    StringWriter writer = new StringWriter();
    render(data, writer);
    return writer.toString();
  }

}
