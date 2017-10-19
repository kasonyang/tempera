package site.kason.tempera.lexer;

import site.kason.klex.TokenRule;
import site.kason.klex.nfa.NFA;

/**
 *
 * @author Kason Yang
 */
public class TexTokenInfo implements TokenRule {

    private int priority;
    private NFA nfa;
    private final TexTokenType type;

    public TexTokenInfo(TexTokenType type, NFA nfa,int priority) {
        this.priority = priority;
        this.nfa = nfa;
        this.type = type;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public NFA getNFA() {
        return nfa;
    }

    public TexTokenType getType() {
        return type;
    }

}
