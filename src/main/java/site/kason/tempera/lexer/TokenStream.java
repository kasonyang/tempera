package site.kason.tempera.lexer;

import site.kason.tempera.lex.LexException;

/**
 *
 * @author Kason Yang
 */
public interface TokenStream {
  
  public TexToken nextToken() throws LexException;
  
}
