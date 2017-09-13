package site.kason.tempera.lex.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Kason Yang
 */
public class State {
    
    private Map<Integer,Set<State>> nextStates = new HashMap();
    
    private Map<CharMatcher,Set<State>> nextStatesByMatcher = new HashMap();
    
    private Set<State> closureStates = new HashSet();
    
    public void pushNextState(int input,State nextState){
        Set<State> list = nextStates.get(input);
        if(list==null){
            list = new HashSet();
            nextStates.put(input, list);
        }
        list.add(nextState);
    }
    
    public void pushNextState(CharMatcher matcher,State nextState){
        Set<State> list = this.nextStatesByMatcher.get(matcher);
        if(list==null){
            list = new HashSet();
            nextStatesByMatcher.put(matcher, list);
        }
        list.add(nextState);
    }
    
    public State[] getNextStates(int input){
        Set<State> result=  new HashSet();
        Set<State> list = nextStates.get(input);
        if(list!=null){
            result.addAll(list);
        }
        if(!this.nextStatesByMatcher.isEmpty()){
            for(Map.Entry<CharMatcher, Set<State>> e:this.nextStatesByMatcher.entrySet()){
                if(e.getKey().isMatched(input)){
                    result.addAll(e.getValue());
                }
            }
        }
        return result.toArray(new State[result.size()]);
    }
    
    public State[] getLambdaClosureStates(){
        return closureStates.toArray(new State[closureStates.size()]);
    }
    
    public void pushLambdaClosureState(State state){
        this.closureStates.add(state);
    }
    
}
