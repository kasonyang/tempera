package site.kason.tempera.parser;

import site.kason.klex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class LexerException extends ParseException {

    public LexerException(OffsetRange offset, String message) {
        super(offset, message);
    }

}
