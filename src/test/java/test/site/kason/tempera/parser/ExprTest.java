package test.site.kason.tempera.parser;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.engine.Configuration;
import site.kason.tempera.engine.Template;
import site.kason.tempera.engine.Engine;
import site.kason.tempera.extension.Function;

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
  
  @Test
  public void testFunction() throws IOException{
    assertRender("4", "{{length(\"test\")}}", Collections.EMPTY_MAP);
  }

  private void assertRender(String expected, String tpl, Map<String, Object> data) throws IOException {
    Engine engine = new Engine();
    engine.addFunction(new Function(){
      @Override
      public String getName() {
        return "length";
      }

      @Override
      public Class<?>[] getParameters() {
        return new Class[]{String.class};
      }

      @Override
      public Object execute(Object[] arguments) {
        return String.valueOf(arguments[0]).length();
      }

      @Override
      public Class<?> getReturnType() {
        return Integer.class;
      }
    
    });   
    Template template = engine.compileInline(tpl, TexTemplateParserTest.class.getSimpleName(), null);
    String out = template.render(data);
    assertEquals(expected, out);
  }

}
