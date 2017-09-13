package test.site.kason.tempera.lexer;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.lex.Token;
import site.kason.tempera.lexer.TexLexer;
import static site.kason.tempera.lexer.TexTokenType.*;
import site.kason.tempera.lexer.TexTokenType;
import test.site.kason.tempera.TestBase;

/**
 *
 * @author Kason Yang
 */
public class TexLexerTest extends TestBase {
    
    public TexLexerTest() {
    }
    
    @Test
    public void test() throws LexException{
        String template = "hello,{{name \"name\"}}";
        TexLexer lexer = new TexLexer(template);
        List<TexTokenType> tokenTypes = this.getTokenTypes(lexer.nextTokens());
        assertEquals(Arrays.asList(TEXT,START_TAG,IDENTITY,SPACE,STRING,END_TAG), tokenTypes);
    }
    
}
