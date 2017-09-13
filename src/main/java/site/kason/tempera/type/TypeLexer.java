package site.kason.tempera.type;

import site.kason.tempera.lex.CharStream;
import site.kason.tempera.lex.Lexer;
import site.kason.tempera.lex.StringCharStream;

/**
 *
 * @author Kason Yang
 */
public class TypeLexer extends Lexer<TypeToken, TypeTokenInfo>{

    public TypeLexer(String input) {
        this(new StringCharStream(input));
    }

    public TypeLexer(CharStream charBuffer) {
        super(charBuffer, TypeTokenInfo.values(), new TypeTokenFactory());
    }
    
    
    

}
