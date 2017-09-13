package site.kason.tempera.parser;

import site.kason.tempera.lex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class SemanticException extends ParseException {

    public SemanticException(OffsetRange offset, String message) {
        super(offset, message);
    }

}
