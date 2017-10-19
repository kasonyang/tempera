package site.kason.tempera.type;

import javax.annotation.Nullable;
import site.kason.klex.TokenRule;
import site.kason.klex.nfa.NFA;
import site.kason.klex.util.NFAUtil;

/**
 *
 * @author Kason Yang
 */
public enum TypeTokenInfo implements TokenRule {
    
    LEFT_DELIMIT("<"),
    RIGHT_DELIMIT(">"),
    COMMA(","),
    LBRACK("["),
    RBRACK("]"),
    NAME(
            NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.oneOfString(".","_"))
                .concat(
                        NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.oneOfString(".","_")).or(NFAUtil.range('0', '9')).closure()
                )
    ),
    EOI((NFA)null)
    
    ;

    private final NFA nfa;
    
    
    private TypeTokenInfo(String text){
        this(NFAUtil.oneOfString(text));
    }

    private TypeTokenInfo(NFA nfa) {
        this.nfa = nfa;
    }

    @Override
    public int getPriority() {
        return this.ordinal();
    }

    @Nullable
    @Override
    public NFA getNFA() {
        return nfa;
    }

}
