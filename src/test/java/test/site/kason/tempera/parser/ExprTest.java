package test.site.kason.tempera.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.engine.Template;
import site.kason.tempera.engine.Engine;

/**
 *
 * @author Kason Yang
 */
public class ExprTest {

  public ExprTest() {
  }

  @Test
  public void testExpr() throws IOException {
    Map<String, Object> data = new HashMap();
    data.put("age", 2);
    String header = "{{var age:Integer}}";
    assertRender("2", header + "{{age}}", data);
    assertRender("3", header + "{{age+1}}", data);
    assertRender("1", header + "{{age-1}}", data);
    assertRender("4", header + "{{age*2}}", data);
    assertRender("1", header + "{{age/2}}", data);
    assertRender("0", header + "{{age%2}}", data);
    assertRender("true", header+"{{age==2}}", data);
    assertRender("false", header+"{{age!=2}}", data);
    assertRender("true", header+"{{age>=2}}", data);
    assertRender("false", header+"{{age>2}}", data);
    assertRender("true", header+"{{age<=2}}", data);
    assertRender("false", header+"{{age<2}}", data);
    assertRender("6.6",header+"{{6.6}}",data);
  }

  private void assertRender(String expected, String tpl, Map<String, Object> data) throws IOException {
    Engine engine = new Engine();
    Template template = engine.compileInline(tpl, TexTemplateParserTest.class.getSimpleName(), null);
    String out = template.render(data);
    assertEquals(expected, out);
  }

}
