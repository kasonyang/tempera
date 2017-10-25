package site.kason.tempera.lexer;

import site.kason.klex.LexException;

/**
 *
 * @author Kason Yang
 */
public interface TokenStream {
  
  /**
   * Get the next token
   * @return the token
   * @throws LexException if lex exception occurs 
   */
  public TexToken nextToken() throws LexException;
  
}
