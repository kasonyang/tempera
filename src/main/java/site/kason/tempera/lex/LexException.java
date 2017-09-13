package site.kason.tempera.lex;
/**
 *
 * @author Kason Yang
 */
public class LexException extends Exception{

    private final OffsetRange offset;

    public LexException(OffsetRange offset,String message) {
        super(message);
        this.offset = offset;
    }

    public OffsetRange getOffset() {
        return offset;
    }
    
}
