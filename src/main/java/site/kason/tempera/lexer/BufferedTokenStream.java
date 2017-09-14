package site.kason.tempera.lexer;

import java.util.ArrayList;
import java.util.List;
import site.kason.tempera.lex.LexException;

/**
 *
 * @author Kason Yang
 */
public class BufferedTokenStream implements TokenStream {
  
  TokenStream tokenStream;
  
  List<TexToken> bufferedTokens = new ArrayList();

  public BufferedTokenStream(TokenStream tokenStream) {
    this.tokenStream = tokenStream;
  }

  @Override
  public TexToken nextToken() throws LexException {
    if(bufferedTokens.isEmpty()){
      return tokenStream.nextToken();
    }else{
      return bufferedTokens.remove(0);
    }
  }
  
  public TexToken LA(int offset) throws LexException{
    if(offset<=0){
      throw new IllegalArgumentException("offset should be greater than 0");
    }
    while(bufferedTokens.size()<offset){
      //TODO maybe EOF
      TexToken next = tokenStream.nextToken();
      bufferedTokens.add(next);
    }
    return bufferedTokens.get(offset-1);
  }

}
