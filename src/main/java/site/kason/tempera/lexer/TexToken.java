package site.kason.tempera.lexer;

import site.kason.klex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class TexToken{

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
