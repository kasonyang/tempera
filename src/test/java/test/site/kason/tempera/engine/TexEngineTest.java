package test.site.kason.tempera.engine;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.engine.Configuration;
import site.kason.tempera.loader.StringTemplateLoader;
import site.kason.tempera.engine.Template;
import site.kason.tempera.engine.Engine;
import site.kason.tempera.parser.TemplateClassLoader;

/**
 *
 * @author Kason Yang
 */
public class TexEngineTest {

  public TexEngineTest() {
  }

  private String buildHeader(Map<String, Object> data) {
    if (data == null || data.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, Object> e : data.entrySet()) {
      String k = e.getKey();
      sb.append("{{var ")
              .append(k).append(":").append(e.getValue().getClass().getName()).append("}}");
    }
    return sb.toString();
  }

  @Test
  public void test() throws Exception {
    Engine engine = new Engine();
    Template tpl = engine.compileInline("{{var name:String}}hello,{{name}}!", "hello.template");
    StringWriter stringWriter = new StringWriter();
    tpl.render(Collections.singletonMap("name", "world"),stringWriter);
    String result = stringWriter.toString();
    assertEquals("hello,world!", result);
  }
  
  @Test
  public void testResources() throws Exception{
    String[] names = new String[]{"A","B","C"};
    List<String> list = Arrays.asList(names);
    Engine engine = new Engine();
    Template tpl = engine.compile("templates.main");
    tpl.render(Collections.singletonMap("names", list),new StringWriter());
  }

  @Test
  public void testLayout() throws Exception {
    File templateOutDir = new File("build/template-compiled");
    FileUtils.forceMkdir(templateOutDir);
    StringTemplateLoader tpls = new StringTemplateLoader();
    Configuration conf = new Configuration();
    conf.setTemplateLoader(tpls);
    conf.setCacheDir(templateOutDir.getAbsolutePath());
    Engine engine = new Engine(conf);

    Map<String, Object> data = Collections.singletonMap("name", "Test");
    tpls.addSource("master", "hello,{{placeholder name}}Tex{{/placeholder}}.");
    tpls.addSource("noreplace", "{{layout master}}{{/layout}}");
    tpls.addSource("replace", this.buildHeader(data) + "{{layout master}}{{replace name}}{{name}}{{/replace}}{{/layout}}");
    String result = engine.compile("noreplace").render(Collections.EMPTY_MAP);
    assertEquals(result, "hello,Tex.");
    String result2 = engine.compile("replace").render(data);
    assertEquals("hello,Test.", result2);

    Map<String, Object> datafor = Collections.singletonMap("list", Arrays.asList(1, 2, 3));
    String masterForTpl = "{{var list:java.util.List}}{{for v in list}}{{placeholder names}}{{v}}{{/placeholder}}{{/for}}";
    tpls.addSource("masterfor", masterForTpl);
    tpls.addSource("forvar", "{{layout masterfor}}{{replace names}}{{v}}+{{/replace}}{{/layout}}");
    String resultMaster = engine.compile("masterfor").render(datafor);
    assertEquals("123", resultMaster);
    String resultFor = engine.compile("forvar").render(datafor);
    assertEquals("1+2+3+", resultFor);

  }
  
  @Test
  public void testReload() throws IOException{
    String tplName = "hello";
    Engine engine = new Engine();
    Template tpl = engine.compileInline("hello",tplName);
    Template tpl2 = engine.compileInline("hi", tplName);
    assertEquals("hello", tpl.render(Collections.EMPTY_MAP));
    assertEquals("hi", tpl2.render(Collections.EMPTY_MAP));
  }

}
