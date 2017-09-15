package test.site.kason.tempera.parser;

import java.io.IOException;
import kalang.AstNotFoundException;
import kalang.ast.ClassNode;
import kalang.compiler.AstLoader;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.engine.TemplateAstLoader;
import site.kason.tempera.engine.TemplateNotFoundException;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.parser.TemplateClassLoader;
import site.kason.tempera.parser.Renderer;
import site.kason.tempera.parser.TemplateParser;

/**
 *
 * @author Kason Yang
 */
public class TexParserTest {
    
    public TexParserTest() {
    }
    
    @Test
    public void test() throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("{{var name:java.lang.String}}Hello,{{name}}!");
        TemplateParser parser = new TemplateParser("test",sb.toString(),new TemplateAstLoader() {
            @Override
            public ClassNode loadTemplateAst(String templateName) throws TemplateNotFoundException, LexException, IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ClassNode loadAst(String className) throws AstNotFoundException {
                return AstLoader.BASE_AST_LOADER.loadAst(className);
            }
        },new TemplateClassLoader());
        Class<Renderer> clazz = parser.parse();
        assertNotNull(clazz);
    }
    
}
