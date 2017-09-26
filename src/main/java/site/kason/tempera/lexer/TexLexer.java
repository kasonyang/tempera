package site.kason.tempera.lexer;

import java.util.LinkedList;
import java.util.List;
import kamons.array.ArrayUtil;
import kamons.string.LiteralParser;
import site.kason.tempera.lex.CharStream;
import site.kason.tempera.lex.Lexer;
import site.kason.tempera.lex.OffsetRange;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.lex.StringCharStream;
import site.kason.tempera.lex.TokenFactory;
import site.kason.tempera.lex.nfa.NFA;
import site.kason.tempera.lex.nfa.NFAUtil;
import static site.kason.tempera.lexer.TexTokenType.*;

/**
 *
 * @author Kason Yang
 */
public class TexLexer {

  private Lexer<TexToken, TexTokenInfo> tagLexer;

  public static final LiteralParser LITERAL_PARSER = LiteralParser.createDefault();

  private static TexTokenInfo tk(TexTokenType token, int priority, NFA nfa) {
    return new TexTokenInfo(token, nfa, priority);
  }

  private static TexTokenInfo tk(TexTokenType token, int priority, String rule) {
    return tk(token, priority, NFAUtil.of(rule));
  }

  private static TexTokenInfo[] getTokenInfos(String startTag, String endTag) {
    int p = 0;
    return new TexTokenInfo[]{
      tk(SPACE, p++, " "),
      tk(DOT, p++, "."),
      tk(ARROW,p++,"->"),
      tk(IN, p++, "in"),
      tk(START_TAG, p++, startTag),
      tk(END_TAG, p++, endTag),
      tk(COLON, p++, ":"),
      tk(VAR, p++, "var"),
      tk(VAL, p++, "val"),
      tk(IF, p++, "if"),
      tk(ELSE, p++, "else"),
      tk(END_IF, p++, "/if"),
      tk(FOR, p++, "for"),
      tk(END_FOR, p++, "/for"),
      tk(PLACEHOLDER, p++, "placeholder"),
      tk(END_PLACEHOLDER, p++, "/placeholder"),
      tk(REPLACE, p++, "replace"),
      tk(END_REPLACE, p++, "/replace"),
      tk(LAYOUT, p++, "layout"),
      tk(END_LAYOUT, p++, "/layout"),
      tk(LPAREN, p++, "("),
      tk(RPAREN, p++, ")"),
      tk(LBRACK, p++, "["),
      tk(RBRACK, p++, "]"),
      tk(LBRACE, p++, "{"),
      tk(RBRACE, p++, "}"),
      tk(LT, p++, "<"),
      tk(GT, p++, ">"),
      tk(AT, p++, "@"),
      tk(LOGIC_NOT, p++, "!"),
      tk(COMMA, p++, ","),
      tk(ADD, p++, "+"),
      tk(SUB, p++, "-"),
      tk(MUL, p++, "*"),
      tk(DIV, p++, "/"),
      tk(MOD, p++, "%"),
      tk(LOGIC_AND, p++, "&&"),
      tk(LOGIC_OR, p++, "||"),
      tk(EQ, p++, "=="),
      tk(LT, p++, "<"),
      tk(LE, p++, "<="),
      tk(GT, p++, ">"),
      tk(GE, p++, ">="),
      tk(NE, p++, "!="),
      tk(PIPE,p++,"|"),
      tk(NUMBER, p++,createNumberNFA()),
      tk(IDENTITY, p++, NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z'))
      .concat(NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).closure())),
      tk(STRING, p++,
      NFAUtil.of("\"").concat(
      NFAUtil.exclude('"', '\\').or(
      NFAUtil.of("\\").concat(NFAUtil.oneOf(ArrayUtil.toInts(LITERAL_PARSER.getSupportedEscapeChars())))
      ).closure()
      ).concat(
      NFAUtil.of("\"")
      )
      )
    };
  }
  private final String input;

  private boolean inTagMode = false;

  private String startTag = "{{";

  private String endTag = "}}";
  
  private final String COMMENT_START = "*",COMMENT_END = "*";
  

  private final CharStream charStream;

  public TexLexer(String input) {
    charStream = new StringCharStream(input);
    this.tagLexer = new Lexer<>(charStream, getTokenInfos(this.startTag, this.endTag), new TokenFactory<TexToken, TexTokenInfo>() {
      @Override
      public TexToken createToken(TexTokenInfo tokenType,
              OffsetRange offset, int[] chars) {
        return new TexToken(tokenType.getType(), offset, new String(chars, 0, chars.length));
      }

      @Override
      public TexToken createEOIToken(OffsetRange offset) {
        return new TexToken(TexTokenType.EOF, offset, "");
      }

    });
    this.input = input;
  }

  public TexToken nextToken() throws LexException {
    int startOffset = this.charStream.getCurrentOffset();
    //if(offset>=this.input.length()) return new TexToken(EOF, offset, offset, "");
    if (startOffset >= this.input.length()) {
      return tagLexer.nextToken();
    }
    int startLine = charStream.getCurrentLine();
    int startColumn = charStream.getCurrentColumn();
    if (!inTagMode) {
      int tagOffset = input.indexOf(this.startTag, startOffset);
      if (tagOffset == startOffset) {
        String commentStartStr = this.startTag+COMMENT_START;
        String commentEndStr = COMMENT_END + this.endTag;
        if(input.startsWith(commentStartStr,startOffset)){
          int commentEndStartOffset = input.indexOf(commentEndStr,startOffset+commentStartStr.length());
          if(commentEndStartOffset>0){
            int stopOffset = commentEndStartOffset + commentEndStr.length()-1;
            int commentLen = stopOffset-startOffset+1;
            charStream.skip(commentLen-1);
            int stopLine = charStream.getCurrentLine();
            int stopColumn = charStream.getCurrentColumn();
            charStream.skip(1);
            return new TexToken(COMMENT,
                new OffsetRange(startOffset,stopOffset,startLine,startColumn,stopLine,stopColumn),input.substring(startOffset,stopOffset+1)
            );
          }else{//comment end string not found
            return this.createTextToken(input.length()-startOffset);
          }
        }else{
          inTagMode = true;
          tagLexer.skip(this.startTag.length());
          int stopOffset = charStream.getCurrentOffset() - 1;
          int stopLine = charStream.getCurrentLine();
          int stopColumn = charStream.getCurrentColumn() - 1;
          return new TexToken(START_TAG,
                  new OffsetRange(startOffset, stopOffset, startLine, startColumn, stopLine, stopColumn), input.substring(startOffset, stopOffset));
        }
      } else if (tagOffset > startOffset) {
        int textLen = tagOffset - startOffset;
        //TODO should skil using tagLexer?
        charStream.skip(textLen);
        return new TexToken(TEXT,
                new OffsetRange(startOffset, charStream.getCurrentOffset() - 1, startLine, startColumn, charStream.getCurrentLine(), charStream.getCurrentColumn() - 1), input.substring(startOffset, startOffset + textLen));
      } else {
        int textLen = input.length() - startOffset;
        tagLexer.skip(textLen);
        return new TexToken(TEXT,
                new OffsetRange(startOffset, charStream.getCurrentOffset() - 1, startLine, startColumn, charStream.getCurrentLine(), charStream.getCurrentColumn() - 1
                ), input.substring(startOffset, startOffset + textLen));
      }
    } else {
      //tagLexer.skip(offset - tagLexer.getCaret());
      TexToken tk = tagLexer.nextToken();
      if (TexTokenType.END_TAG.equals(tk.getTokenType())) {
        inTagMode = false;
      }
      return tk;
    }
  }

  public List<TexToken> nextTokens() throws LexException {
    LinkedList<TexToken> result = new LinkedList();
    TexToken tk;
    while ((tk = this.nextToken()).getTokenType() != EOF) {
      result.add(tk);
    }
    return result;
  }
  
  private static NFA createNumberNFA(){
    NFA d1 = NFAUtil.range('0', '9');
    NFA d2 = NFAUtil.range('0', '9');
    NFA d3 = NFAUtil.range('0', '9');
    NFA d4 = NFAUtil.range('0', '9');
    NFA d5 = NFAUtil.range('0', '9');
    NFA d6 = NFAUtil.range('0', '9');
    NFA dot = NFAUtil.of(".");
    NFA intNFA = d1.concat(d2.closure());
    NFA floatNFA = d3.concat(d4.closure()).concat(dot).concat(d5.concat(d6.closure()));
    return intNFA.or(floatNFA);
  }
  
  private TexToken createTextToken(int len) {
    int startOffset = charStream.getCurrentOffset();
    int startLine = charStream.getCurrentLine();
    int startColumn = charStream.getCurrentColumn();
    charStream.skip(len - 1);
    int stopOffset = charStream.getCurrentOffset();
    int stopLine = charStream.getCurrentLine();
    int stopColumn = charStream.getCurrentColumn();
    charStream.skip(1);
    return new TexToken(TEXT,
            new OffsetRange(startOffset, stopOffset, startLine, startColumn, stopLine, charStream.getCurrentColumn() - 1
            ), input.substring(startOffset, startOffset + len));
  }

}
