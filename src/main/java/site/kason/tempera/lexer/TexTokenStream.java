package site.kason.tempera.lexer;

import site.kason.klex.LexException;

/**
 *
 * @author Kason Yang
 */
public class TexTokenStream implements TokenStream {

  private TexLexer lexer;

  private int channel;

  public TexTokenStream(TexLexer lexer, int channel) {
    this.lexer = lexer;
    this.channel = channel;
  }

  public TexTokenStream(TexLexer lexer) {
    this(lexer, TexTokenType.CHANNEL_DEFAULT);
  }

  @Override
  public TexToken nextToken() throws LexException {
    TexToken tk;
    do {
      tk = lexer.nextToken();
    } while (tk.getTokenType().getChannel() != channel && !TexTokenType.EOF.equals(tk.getTokenType()));
    return tk;
  }

}
