package site.kason.tempera.type;

import site.kason.klex.CharStream;
import site.kason.klex.Klexer;
import site.kason.klex.StringCharStream;

/**
 *
 * @author Kason Yang
 */
public class TypeLexer extends Klexer<TypeToken, TypeTokenInfo> {

  public TypeLexer(String input) {
    this(new StringCharStream(input));
  }

  public TypeLexer(CharStream charBuffer) {
    super(charBuffer, TypeTokenInfo.values(), new TypeTokenFactory());
  }

}
