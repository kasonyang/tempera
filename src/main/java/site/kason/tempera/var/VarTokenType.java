package site.kason.tempera.var;

import javax.annotation.Nullable;
import site.kason.tempera.lex.TokenInfo;
import site.kason.tempera.lex.nfa.NFA;
import site.kason.tempera.lex.nfa.NFAUtil;

/**
 *
 * @author Kason Yang
 */
public enum VarTokenType implements TokenInfo {
    
    VAR("var"),
    COLON(":"),
    SEMICOLON(";"),
    IDENTITY(
            NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.of("_"))
                .concat(
                        NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.of("_")).or(NFAUtil.range('0', '9')).closure()
                )
    ),
    TYPE(
            NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.oneOf(".","_"))
                .concat(
                        NFAUtil.range('a', 'z').or(NFAUtil.range('A', 'Z')).or(NFAUtil.oneOf(".","_")).or(NFAUtil.range('0', '9')).closure()
                )
    ),
    EOI((NFA)null)
    
    ;

    private final NFA nfa;
    
    
    private VarTokenType(String text){
        this(NFAUtil.oneOf(text));
    }

    private VarTokenType(NFA nfa) {
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
