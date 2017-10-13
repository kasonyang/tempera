package test.site.kason.tempera.lexer;

import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.lexer.TexLexer;
import site.kason.tempera.lexer.TexTokenStream;
import site.kason.tempera.lexer.TokenStream;
import static site.kason.tempera.lexer.TexTokenType.*;
/**
 *
 * @author Kason Yang
 */
public class TokenStreamTest {
    
    public TokenStreamTest() {
    }
    
    @Test
    public void test() throws LexException{
        TexLexer lexer = new TexLexer("hello,{{name}}!","{{","}}");
        TokenStream ts  = new TexTokenStream(lexer);
        assertEquals(TEXT, ts.nextToken().getTokenType());
        assertEquals(START_TAG, ts.nextToken().getTokenType());
        assertEquals(IDENTITY, ts.nextToken().getTokenType());
        assertEquals(END_TAG, ts.nextToken().getTokenType());
        assertEquals(TEXT, ts.nextToken().getTokenType());        
    }
    
}
