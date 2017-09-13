package site.kason.tempera.parser;

import java.util.Objects;
import site.kason.tempera.lex.OffsetRange;


/**
 *
 * @author Kason Yang
 */
public abstract class ParseException extends RuntimeException {
    
    private OffsetRange offset;
    
    protected ParseException(OffsetRange offset,String message,Throwable throwable){
        super(message, throwable);
        this.offset = offset;
    }

    public ParseException(OffsetRange offset,String message) {
        super(message);
        this.offset = offset;
    }

    public OffsetRange getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return Objects.toString(offset) + this.getMessage();
    }

}
