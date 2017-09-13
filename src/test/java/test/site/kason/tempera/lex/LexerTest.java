package test.site.kason.tempera.lex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.lex.Lexer;
import site.kason.tempera.lex.OffsetRange;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.lex.StringCharStream;
import site.kason.tempera.lex.TokenFactory;
import site.kason.tempera.lex.TokenInfo;
import site.kason.tempera.lex.nfa.NFA;
import site.kason.tempera.lex.nfa.NFAUtil;

/**
 *
 * @author Kason Yang
 */
public class LexerTest {
    
    static class TTokenInfo implements TokenInfo{
        private int priority;
        private NFA nfa;

        public TTokenInfo(int priority, NFA nfa) {
            this.priority = priority;
            this.nfa = nfa;
        }
        @Override
        public int getPriority() {
            return priority;
        }
        @Override
        public NFA getNFA() {
            return nfa;
        }
    }
    
    static TokenInfo TI(int priority,NFA nfa){
        return new TTokenInfo(priority, nfa);
    }
    
    final static TokenInfo[] TOKENS = new TokenInfo[]{
        TI(0,NFAUtil.oneOf(" ")),
        TI(1,NFAUtil.oneOf("if")),
        TI(2,NFAUtil.oneOf("for")),
        TI(3,NFAUtil.oneOf("do")),
        TI(4,NFAUtil.range('a', 'z')
                .concat(NFAUtil.range('a', 'z').closure())),
    };
    
    public LexerTest() {
    }
    
    private List<Integer> parse(String input) throws LexException{
        Lexer<Integer,TTokenInfo> lexer = new Lexer(new StringCharStream(input),TOKENS,new TokenFactory<Integer,TTokenInfo>(){
            @Override
            public Integer createToken(TTokenInfo tokenInfo, OffsetRange offset, int[] chars) {
                return tokenInfo.priority;
            } 

            @Override
            public Integer createEOIToken(OffsetRange offset) {
                return -1;
            }
        });
        List<Integer> tokens = lexer.nextTokens();
        List<Integer> types = new ArrayList(tokens.size());
        for(Integer t:tokens){
            types.add(t);
        }
        return types;
    }
    
    @Test
    public void test() throws LexException{
        
        assertEquals(Arrays.asList(1,0,2,0,3,0,4),parse("if for do x"));
        assertEquals(Arrays.asList(1,0,2,0,3,0,4),parse("if for do xx"));
        assertEquals(Arrays.asList(1,0,2,0,3,0,4),parse("if for do xxxxxxx"));
        assertEquals(Arrays.asList(1,0,4,0,3),parse("if xxx do"));
        assertEquals(Arrays.asList(4),parse("iffordo"));
    }
    
}
