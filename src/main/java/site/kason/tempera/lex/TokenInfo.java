package site.kason.tempera.lex;

import site.kason.tempera.lex.nfa.NFA;

/**
 *
 * @author Kason Yang
 */
public interface TokenInfo {
    
    public int getPriority();    
    
    public NFA getNFA();
    
}
