package site.kason.tempera.lexer;

import kamons.array.ArrayUtil;
import site.kason.klex.nfa.NFA;
import static site.kason.klex.util.NFAUtil.*;
import static site.kason.tempera.lexer.TexLexer.LITERAL_PARSER;

/**
 *
 * @author Kason Yang
 */
public class TexNFA {

  public static NFA createString() {
    int[] escapeChars = ArrayUtil.toInts(LITERAL_PARSER.getSupportedEscapeChars());
    NFA escapeNFA = ofString("\\").concat(oneOf(escapeChars));
    NFA part = exclude('"', '\\').or(escapeNFA).closure();
    return ofString("\"").concat(part).concat(ofString("\""));
  }

  public static NFA createIdentity() {
    NFA first = range('a', 'z').or(range('A', 'Z')).or(oneOf('_'));
    NFA ele = range('a', 'z').or(range('A', 'Z')).or(range('0', '9')).or(oneOf('_'));
    return first.concat(ele.closure());
  }

  public static NFA createNumber() {
    NFA dot = ofString(".");
    NFA intNFA = oneOf('0').or(range('1', '9').concat(range('0', '9').closure()));
    NFA intNFA2 = copy(intNFA);
    NFA rightPart = range('0', '9').concat(range('0', '9').closure());
    NFA floatNFA = intNFA2.concat(dot).concat(rightPart);
    return intNFA.or(floatNFA);
  }

}
