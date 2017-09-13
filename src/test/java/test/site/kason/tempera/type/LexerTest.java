package test.site.kason.tempera.type;

import java.util.List;
import kalang.AstNotFoundException;
import kalang.core.Type;
import kalang.core.Types;
import static org.junit.Assert.*;
import org.junit.Test;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.type.TypeLexer;
import site.kason.tempera.type.TypeParser;
import site.kason.tempera.type.TypeToken;
import site.kason.tempera.type.TypeTokenInfo;

/**
 *
 * @author Kason Yang
 */
public class LexerTest {
    
    @Test
    public void test() throws LexException{
        TypeLexer lexer = new TypeLexer("List<String>");
        List<TypeToken> tokens = lexer.nextTokens();
        assertEquals(TypeTokenInfo.NAME , tokens.get(0).getType());
        assertEquals(TypeTokenInfo.LEFT_DELIMIT , tokens.get(1).getType());
        assertEquals(TypeTokenInfo.NAME , tokens.get(2).getType());
        assertEquals(TypeTokenInfo.RIGHT_DELIMIT , tokens.get(3).getType());
    }
    
    @Test
    public void testParser() throws AstNotFoundException, LexException{
        TypeParser parser = new TypeParser();
        assertEquals(parser.parse(Types.getRootType().getName()), Types.getRootType());
        assertEquals(parser.parse("java.util.List<java.lang.String>"), 
                Types.getClassType(Types.getClassType("java.util.List").getClassNode(),new Type[]{Types.getStringClassType()}));
                ;
    }

}
