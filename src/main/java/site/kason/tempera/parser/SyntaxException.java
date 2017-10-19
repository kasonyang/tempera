package site.kason.tempera.parser;

import site.kason.klex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class SyntaxException extends ParseException {

    public SyntaxException(OffsetRange offset, String message) {
        super(offset, message);
    }

}
