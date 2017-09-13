package site.kason.tempera.var;

import site.kason.tempera.lex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class VarToken {
    
    String text;

    private final VarTokenType type;
    
    private OffsetRange offset;

    public VarToken(VarTokenType type, OffsetRange offset, String text) {
        this.type = type;
        this.text = text;
        this.offset = offset;
    }

    public VarTokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public OffsetRange getOffset() {
        return offset;
    }
    
}
