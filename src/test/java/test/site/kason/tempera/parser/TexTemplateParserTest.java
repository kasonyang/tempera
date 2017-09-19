package test.site.kason.tempera.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kalang.AstNotFoundException;
import kalang.ast.ClassNode;
import kalang.compiler.AstLoader;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.engine.TemplateAstLoader;
import site.kason.tempera.engine.TemplateNotFoundException;
import site.kason.tempera.extension.Function;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.lexer.TexLexer;
import site.kason.tempera.lexer.TexTokenStream;
import site.kason.tempera.lexer.TokenStream;
import site.kason.tempera.model.RenderContext;
import site.kason.tempera.parser.TemplateClassLoader;
import site.kason.tempera.parser.TemplateParser;
import site.kason.tempera.parser.Renderer;

/**
 *
 * @author Kason Yang
 */
public class TexTemplateParserTest {

  public String name = "TEST";

  public String[] names = new String[]{"T", "E", "S", "T"};

  public String title() {
    return name;
  }

  public String getText() {
    return name;
  }

  public TexTemplateParserTest() {
  }

  private String render(String tpl, Map<String, Object> data, Map<String, String> types)
          throws Exception {
    TexLexer lexer = new TexLexer(tpl);
    TokenStream ts = new TexTokenStream(lexer);
    TemplateParser parser = new TemplateParser("Test", ts, new TemplateAstLoader() {
      @Override
      public ClassNode loadTemplateAst(String templateName) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public ClassNode loadAst(String className) throws AstNotFoundException {
        return AstLoader.BASE_AST_LOADER.loadAst(className);
      }
    }, new TemplateClassLoader());
    for (Map.Entry<String, String> e : types.entrySet()) {
      parser.setVarType(e.getKey(), e.getValue());
    }
    Class<Renderer> tplClazz = parser.parse();
    Renderer inst = tplClazz.newInstance();
    StringWriter writer = new StringWriter();
    inst.render(data,writer,new RenderContext());
    return writer.toString();
  }

  @Test
  public void test() throws Exception {
    String tpl = "hello,{{if name}}{{name}}{{else}}world{{/if}}!";
    assertEquals("hello,test!", render(tpl, Collections.singletonMap("name", "test"), Collections.singletonMap("name", String.class.getName())));
    assertEquals("hello,world!", render(tpl, Collections.EMPTY_MAP, Collections.singletonMap("name", String.class.getName())));
    assertEquals(this.name, render("{{if obj.text}}{{obj.text}}{{/if}}", Collections.singletonMap("obj", new TexTemplateParserTest()), Collections.singletonMap("obj", TexTemplateParserTest.class.getName())));
  }

  @Test
  public void testFor() throws Exception {
    String tpl = "{{for it in list}}{{it.name}}{{/for}}";
    String tpl2 = "{{var list:List<" + TexTemplateParserTest.class.getName() + ">}}" + tpl;
    Map<String, Object> data = new HashMap();
    TexTemplateParserTest it1 = new TexTemplateParserTest();
    TexTemplateParserTest it2 = new TexTemplateParserTest();
    TexTemplateParserTest it3 = new TexTemplateParserTest();
    it1.name = "1";
    it2.name = "2";
    it3.name = "3";
    data.put("list", Arrays.asList(it1, it2, it3));
    assertEquals("123", render(tpl, Collections.singletonMap("list", new TexTemplateParserTest[]{it1, it2, it3}), Collections.singletonMap("list", TexTemplateParserTest.class.getName() + "[]")
    ));
    assertEquals("0T1E2S3T", render("{{ for n,ctx in model.names }}{{ctx.index}}{{n}}{{/for}}", Collections.singletonMap("model", it1), Collections.singletonMap("model", it1.getClass().getName())));
    assertEquals("123", render(tpl, data, Collections.singletonMap("list", "java.util.List<" + TexTemplateParserTest.class.getName() + ">")));
    assertEquals("123", render(tpl2, data, Collections.EMPTY_MAP));
  }

  @Test
  public void testProperty() throws Exception {
    String tpl = "hello,{{this.name}}{{this.title}}{{this.text}}!";
    TexTemplateParserTest test = new TexTemplateParserTest();
    String result = this.render(tpl, Collections.singletonMap("this", test), Collections.singletonMap("this", TexTemplateParserTest.class.getName()));
    assertEquals("hello,TESTTESTTEST!", result);
  }

  @Test
  public void testPlaceholder() throws Exception {
    String tpl = "hello,{{placeholder name}}Tex{{/placeholder}}.";
    String result = this.render(tpl, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    assertEquals("hello,Tex.", result);
  }

}
