package site.kason.tempera.lexer;

import site.kason.tempera.lex.LexException;

/**
 *
 * @author Kason Yang
 */
public class TokenStream {
    
    private TexLexer lexer;
    
    private int channel;

    public TokenStream(TexLexer lexer,int channel) {
        this.lexer = lexer;
        this.channel = channel;
    }

    public TokenStream(TexLexer lexer) {
        this(lexer,TexTokenType.CHANNEL_DEFAULT);
    }
    
    public TexToken nextToken() throws LexException{
        TexToken tk;
        do{
            tk = lexer.nextToken();
        }while(tk.getTokenType().getChannel()!=channel && !TexTokenType.EOF.equals(tk.getTokenType()));
        return tk;
    }

}
