package site.kason.tempera.parser;

import site.kason.tempera.lex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class LexerException extends ParseException {

    public LexerException(OffsetRange offset, String message) {
        super(offset, message);
    }

}
