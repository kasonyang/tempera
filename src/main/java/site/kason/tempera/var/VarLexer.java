package site.kason.tempera.var;

import site.kason.tempera.lex.CharStream;
import site.kason.tempera.lex.Lexer;
import site.kason.tempera.lex.StringCharStream;

/**
 *
 * @author Kason Yang
 */
public class VarLexer extends Lexer<VarToken, VarTokenType>{

    public VarLexer(String input) {
        this(new StringCharStream(input));
    }

    public VarLexer(CharStream charBuffer) {
        super(charBuffer, VarTokenType.values(), new VarTokenFactory());
    }
    
    
    

}
