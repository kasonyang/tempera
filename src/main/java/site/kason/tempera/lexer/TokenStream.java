package site.kason.tempera.lexer;

import site.kason.klex.LexException;

/**
 *
 * @author Kason Yang
 */
public interface TokenStream {
  
  public TexToken nextToken() throws LexException;
  
}
