package site.kason.tempera.parser;

import site.kason.tempera.lex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class UnknownException extends ParseException{
    
    public UnknownException(Throwable throwable){
        this("",throwable);
    }

    public UnknownException(String message,Throwable throwable){
        this(OffsetRange.NONE,message,throwable);
    }
    
    public UnknownException(OffsetRange offset, String message,Throwable throwable) {
        super(offset, message,throwable);
    }

}
