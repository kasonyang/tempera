package test.site.kason.tempera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import site.kason.tempera.engine.Configuration;
import site.kason.tempera.engine.Engine;
import site.kason.tempera.engine.Template;
import site.kason.tempera.extension.Function;
import site.kason.tempera.lexer.TexTokenType;
import site.kason.tempera.lexer.TexToken;
import test.site.kason.tempera.parser.TexTemplateParserTest;

/**
 *
 * @author Kason Yang
 */
public class TestBase {

  public TestBase() {
  }

  protected List<TexTokenType> getTokenTypes(List<TexToken> tokens) {
    List<TexTokenType> types = new ArrayList(tokens.size());
    for (TexToken t : tokens) {
      types.add(t.getTokenType());
    }
    return types;
  }

  protected void assertRender(String expected, String tpl) throws IOException {
    assertRender(expected, tpl, Collections.EMPTY_MAP);
  }

  protected void assertRender(String expected, String tpl, Map<String, Object> data) throws IOException {
    Configuration conf = new Configuration(Configuration.DEFAULT_HTML);
    conf.registerFunction("length", new Function() {

      @Override
      public Object execute(Object[] arguments) {
        return String.valueOf(arguments[0]).length();
      }

    });
    Engine engine = new Engine(conf);
    Template template = engine.compileInline(tpl, TexTemplateParserTest.class.getSimpleName());
    String out = template.render(data);
    assertEquals(expected, out);
  }

}
