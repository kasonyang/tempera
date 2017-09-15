package site.kason.tempera.lexer;

/**
 *
 * @author Kason Yang
 */
public enum TexTokenType {

  EOF(TexTokenType.CHANNEL_SKIP),
  COMMENT(TexTokenType.CHANNEL_SKIP),
  TEXT,
  SPACE(TexTokenType.CHANNEL_SKIP),
  DOT,
  IN,
  ELSE,
  START_TAG,
  END_TAG,
  IF,
  END_IF,
  FOR,
  END_FOR,
  IDENTITY,
  LPAREN,//(
  RPAREN,//)
  LBRACK,//[
  RBRACK,//]
  LBRACE,//{
  RBRACE,//}
  LT, //<
  GT, //>
  LE, // <=
  GE, // >=
  EQ, // ==
  NE, // !=
  AT,
  LOGIC_NOT,
  LOGIC_AND,
  LOGIC_OR,
  COMMA,//,
  ADD,
  SUB,
  MUL,
  DIV,
  MOD,
  NUMBER,
  PLACEHOLDER,
  END_PLACEHOLDER,
  REPLACE,
  END_REPLACE,
  LAYOUT,
  END_LAYOUT,
  STRING,
  VAR,
  VAL,
  COLON;

  private int channel;

  public final static int CHANNEL_DEFAULT = 0, CHANNEL_SKIP = 1;

  private TexTokenType() {
    this(CHANNEL_DEFAULT);
  }

  private TexTokenType(int channel) {
    this.channel = channel;
  }

  public int getChannel() {
    return channel;
  }

}
