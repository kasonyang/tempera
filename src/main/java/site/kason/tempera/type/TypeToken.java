package site.kason.tempera.type;

import site.kason.tempera.lex.Token;

/**
 *
 * @author Kason Yang
 */
public class TypeToken {
    
    int startOffset;
    int stopOffset;
    String text;
    private final TypeTokenInfo type;

    public TypeToken(TypeTokenInfo type, int startOffset, int stopOffset, String text) {
        this.type = type;
        this.startOffset = startOffset;
        this.stopOffset = stopOffset;
        this.text = text;
    }

    public TypeTokenInfo getType() {
        return type;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getStopOffset() {
        return stopOffset;
    }

    public String getText() {
        return text;
    }
    
    

}
