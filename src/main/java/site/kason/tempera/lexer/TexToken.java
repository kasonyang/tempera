package site.kason.tempera.lexer;

import site.kason.tempera.lex.OffsetRange;
import site.kason.tempera.lex.Token;

/**
 *
 * @author Kason Yang
 */
public class TexToken implements Token<TexTokenType> {

    private final TexTokenType tokenType;
    private OffsetRange offset;
    private final String text;

    public TexToken(TexTokenType tokenType
            ,OffsetRange offset
            , String text) {
        this.tokenType = tokenType;
        this.offset = offset;
        this.text = text;
    }

    @Override
    public TexTokenType getTokenType() {
        return this.tokenType;
    }

    public OffsetRange getOffset() {
        return offset;
    }

    public String getText() {
        return text;
    }

}
