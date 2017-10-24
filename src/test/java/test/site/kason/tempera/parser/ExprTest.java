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
import site.kason.tempera.filters.HtmlFilter;
import site.kason.tempera.filters.RawFilter;
import site.kason.tempera.model.RenderContext;

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
    assertRender("false",header+"{{!age}}",data);
    assertRender("-2",header+"{{-age}}",data);
    assertRender("6.6",header+"{{6.6}}",data);
    assertRender("0",header+"{{age ? 0 : 1}}",data);
    assertRender("1",header+"{{!age ? 0 : 1}}",data);
    assertRender("2",header+"{{age ?: 1}}",data);
    assertRender("1",header+"{{!age ?: 1}}",data);
    assertRender("true",header+"{{age && age}}",data);
    assertRender("true",header+"{{age || age}}",data);
    assertRender("2",header+"{{(age)}}",data);
    assertRender("123",header+"{{for c in [1,2,3]}}{{c}}{{/for}}",data);
  }
  
  @Test
  public void testFunction() throws IOException{
    assertRender("4", "{{length(\"test\")}}", Collections.EMPTY_MAP);
    assertRender("1.23", "{{format(\"%.2f\",1.231111)}}");
  }
  
  @Test
  public void testArrow() throws IOException{
    assertRender("4", "{{\"test\"->length}}", Collections.EMPTY_MAP);
  }
  
  @Test
  public void testStringEscape() throws IOException{
    assertRender("\"", "{{\"\\\"\"|raw}}");
  }
  
  @Test
  public void testEscape() throws IOException{
    assertRender("&&amp;","&{{\"&\"}}",Collections.EMPTY_MAP);
    assertRender("&", "{{\"&\"|raw}}",Collections.EMPTY_MAP);
  }
  
  private void assertRender(String expected,String tpl) throws IOException{
    assertRender(expected, tpl , Collections.EMPTY_MAP);
  }

  private void assertRender(String expected, String tpl, Map<String, Object> data) throws IOException {
    Configuration conf = new Configuration(Configuration.DEFAULT_HTML);
    conf.registerFunction("length",new Function(){

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
