package site.kason.tempera.lex;

import site.kason.tempera.lex.nfa.NFA;

/**
 *
 * @author Kason Yang
 */
public interface TokenType {
    
    public int getPriorty();
    
    public NFA getNFA();
    
    public String getName();
    
}
