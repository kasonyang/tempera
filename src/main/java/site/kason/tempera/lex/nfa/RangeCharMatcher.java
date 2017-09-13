package site.kason.tempera.lex.nfa;
/**
 *
 * @author Kason Yang
 */
public class RangeCharMatcher implements CharMatcher{
    
    private int firstAcceptedChar;
    
    private int lastAcceptedChar;

    public RangeCharMatcher(int firstAcceptedChar, int lastAcceptedChar) {
        this.firstAcceptedChar = firstAcceptedChar;
        this.lastAcceptedChar = lastAcceptedChar;
    }

    @Override
    public boolean isMatched(int character) {
        return character>= firstAcceptedChar && character <= lastAcceptedChar;
    }

}
