package test.site.kason.tempera.lex;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import site.kason.tempera.lex.CharStream;
import site.kason.tempera.lex.StringCharStream;
import site.kason.tempera.lex.nfa.NFA;
import site.kason.tempera.lex.nfa.NFAUtil;
import site.kason.tempera.lex.nfa.State;
import site.kason.tempera.lex.nfa.MatchedResult;

/**
 *
 * @author Kason Yang
 */
public class NFAUtilTest {
    
    public NFAUtilTest() {
    }
    
    @Test
    public void testNFA(){
        CharStream is = new StringCharStream("hello");
        NFA nfa = NFAUtil.oneOf("hello");
        State[] acceptedStates = nfa.getAcceptedStates();
        assertEquals(acceptedStates.length, 1);
        MatchedResult matchedState = nfa.match(is);
        assertNotNull(matchedState);
        //assertTrue(Arrays.asList(acceptedStates).contains(matchedState.getMatchedState()));
    }
    
    @Test
    public void testRangeMatch(){
        NFA nfa = NFAUtil.range('a', 'z');
        assertNotNull(nfa.match(new StringCharStream("a")));
        assertNotNull(nfa.match(new StringCharStream("g")));
        assertNotNull(nfa.match(new StringCharStream("m")));
        assertNotNull(nfa.match(new StringCharStream("z")));
        assertNull(nfa.match(new StringCharStream("A")));
    }
    
    @Test
    public void testOr(){
        StringCharStream is1 = new StringCharStream("hello");
        StringCharStream is2 = new StringCharStream("hi");
        StringCharStream isConcat = new StringCharStream("hellohi");
        StringCharStream is3 = new StringCharStream("not match");
        NFA nfa = NFAUtil.oneOf("hello");
        NFA nfa2 = NFAUtil.oneOf("hi");
        nfa.or(nfa2);
        MatchedResult matched1 = nfa.match(is1);
        MatchedResult matched2 = nfa.match(is2);
        MatchedResult matched3 = nfa.match(is3);
        assertNotNull(matched1);
        assertNotNull(matched2);
        assertNull(matched3);
        nfa.concat(nfa2);
        assertNotNull(nfa.match(isConcat));
        
        nfa.closure();
        assertEquals(null,nfa.match(new StringCharStream("")));
        assertEquals(7,nfa.match(new StringCharStream("hellohi")).getMatchedLength());
        assertEquals(14,nfa.match(new StringCharStream("hellohihellohi")).getMatchedLength());
        assertEquals(21,nfa.match(new StringCharStream("hellohihellohihellohi")).getMatchedLength());
    }
    
}
