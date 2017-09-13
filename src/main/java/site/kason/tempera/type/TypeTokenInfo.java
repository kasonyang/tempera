package site.kason.tempera.type;

import javax.annotation.Nullable;
import site.kason.tempera.lex.TokenInfo;
import site.kason.tempera.lex.nfa.NFA;
import site.kason.tempera.lex.nfa.NFAUtil;

/**
 *
 * @author Kason Yang
 */
public enum TypeTokenInfo implements TokenInfo {
    
    LEFT_DELIMIT("<"),
    RIGHT_DELIMIT(">"),
    COMMA(","),
    LBRACK("["),
    RBRACK("]"),
    NAME(
            NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.oneOf(".","_"))
                .concat(
                        NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.oneOf(".","_")).or(NFAUtil.range('0', '9')).closure()
                )
    ),
    EOI((NFA)null)
    
    ;

    private final NFA nfa;
    
    
    private TypeTokenInfo(String text){
        this(NFAUtil.oneOf(text));
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
