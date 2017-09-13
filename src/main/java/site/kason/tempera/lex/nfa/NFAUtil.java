package site.kason.tempera.lex.nfa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kason Yang
 */
public class NFAUtil {
    
    public static NFA range(int firstAcceptedChar,int lastAcceptedChar){
        State startState = new State();
        State acceptedState = new State();
        startState.pushNextState(new RangeCharMatcher(firstAcceptedChar, lastAcceptedChar), acceptedState);
        return new NFA(startState, Arrays.asList(acceptedState));
    }
    
    public static NFA oneOf(String... str){
        NFA nfa = null;
        for(int i=0;i<str.length;i++){
            NFA theNfa = of(str[i]);
            nfa = (nfa==null) ? theNfa : nfa.or(theNfa);
        }
        return nfa;
    }
    
    public static NFA of(String str){
        State startState = new State();
        State currentState = startState;
        int strLen = str.length();
        int offset = 0;
        while(offset<strLen){
            char nextInput = str.charAt(offset++);
            State nextState = new State();
            currentState.pushNextState(nextInput, nextState);
            currentState = nextState;
        }
        return new NFA(startState, Arrays.asList(currentState));
    }
    
    public static NFA exclude(int... excludes){
        State startState = new State();
        State acceptedState = new State();
        startState.pushNextState(new ExcludeCharMatcher(excludes), acceptedState);
        return new NFA(startState, Arrays.asList(acceptedState));
    }
    
    public static NFA oneOf(int... chars){
        State startState = new State();
        State acceptedState = new State();
        for(int i=0;i<chars.length;i++){
            startState.pushNextState(chars[i], acceptedState);
        }
        return new NFA(startState, Arrays.asList(acceptedState));
    }
    
    public static NFA anyChar(){
        State startState = new State();
        State acceptedState = new State();
        startState.pushNextState(new AnyCharMatcher(), acceptedState);
        return new NFA(startState, Arrays.asList(acceptedState));
    }

}
