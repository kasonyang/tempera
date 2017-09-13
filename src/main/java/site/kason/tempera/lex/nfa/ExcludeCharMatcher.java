package site.kason.tempera.lex.nfa;
/**
 *
 * @author Kason Yang
 */
public class ExcludeCharMatcher implements CharMatcher {
    
    private final int[] excludeChars ;

    public ExcludeCharMatcher(int[] excludeChars) {
        this.excludeChars = excludeChars;
    }

    @Override
    public boolean isMatched(int character) {
        for(int c:this.excludeChars){
            if(character==c) return false;
        }
        return true;
    }

}
