package test.site.kason.tempera;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.lex.Token;
import site.kason.tempera.lexer.TexTokenType;
import site.kason.tempera.lexer.TexToken;

/**
 *
 * @author Kason Yang
 */
public class TestBase {
    
    public TestBase() {
    }
    
    protected List<TexTokenType> getTokenTypes(List<TexToken> tokens){
        List<TexTokenType> types = new ArrayList(tokens.size());
        for(Token<TexTokenType> t:tokens){
            types.add(t.getTokenType());
        }
        return types;
    }
    
}
