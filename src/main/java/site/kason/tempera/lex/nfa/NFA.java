package site.kason.tempera.lex.nfa;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import site.kason.tempera.lex.CharStream;

/**
 *
 * @author Kason Yang
 */
public class NFA {
    
    private State startState;
    
    private List<State> acceptedStates;

    public NFA(State startState, List<State> acceptedStates) {
        this.startState = startState;
        this.acceptedStates = acceptedStates;
    }
    
    private  static  Set<State> getLambdaClosureStates(Set<State> states){
        Queue<State> todos = new LinkedList();
        todos.addAll(states);
        Set<State> results = new HashSet();
        while(!todos.isEmpty()){
            State s = todos.poll();
            if(results.add(s)){
                State[] lambdaStates = s.getLambdaClosureStates();
                todos.addAll(Arrays.asList(lambdaStates));
            }
        }
        return results;
    }
    
    public MatchedResult match(CharStream inputStream){
        Set<State> currentStates = new HashSet();
        currentStates.add(startState);
        currentStates = getLambdaClosureStates(currentStates);
        State[] matchedState =  null;//this.findAcceptedState(currentStates);
        int inputOffset = 1;
        int matchedLen = 0;
        while(!currentStates.isEmpty() && inputStream.lookAhead(inputOffset)!=CharStream.EOF){
            Set<State> nextStates  = new HashSet();
            int input = inputStream.lookAhead(inputOffset++);
            for(State s:currentStates){
                State[] nexts = s.getNextStates(input);
                nextStates.addAll(Arrays.asList(nexts));
            }
            nextStates = getLambdaClosureStates(nextStates);
            State[] found = this.findAcceptedState(nextStates);
            if(found!=null && found.length>0){
                matchedState = found;
                matchedLen = inputOffset-1;
            }
            currentStates = nextStates;
        }
        int[] matchedChars = inputStream.consume(matchedLen);
        return matchedState!=null ? new MatchedResult(matchedState,matchedLen,matchedChars) : null;
    }
    
    private State[] findAcceptedState(Set<State> states){
        List<State> accepteds = new LinkedList();
        for(State s:states){
            if(this.acceptedStates.contains(s)){
                accepteds.add(s);
            }
        }
        return accepteds.toArray(new State[accepteds.size()]);
    }

    public State[] getAcceptedStates() {
        State[] res = (State[]) Array.newInstance(State.class,acceptedStates.size());
        return acceptedStates.toArray(res);
    }

    public State getStartState() {
        return startState;
    }
    
    public  NFA or(NFA nfa2){
        State newStartState = new State();
        newStartState.pushLambdaClosureState(this.getStartState());
        newStartState.pushLambdaClosureState(nfa2.getStartState());
        State[] acList1 = this.getAcceptedStates();
        State[] acList2 = nfa2.getAcceptedStates();
        List<State> newAcceptedStates = new ArrayList(acList1.length + acList2.length);
        newAcceptedStates.addAll(Arrays.asList(acList1));
        newAcceptedStates.addAll(Arrays.asList(acList2));
        this.startState = newStartState;
        this.acceptedStates = newAcceptedStates;
        return this;
    }
    
    
    public NFA concat(NFA nfa2){
        State newStartState = this.getStartState();
        State[] accpetedState = this.getAcceptedStates();
        for(State ac:accpetedState){
            ac.pushLambdaClosureState(nfa2.getStartState());
        }
        this.startState = newStartState;
        this.acceptedStates =Arrays.asList(nfa2.getAcceptedStates());
        return this;
    }
    
    public NFA closure(){
        State[] acList = this.getAcceptedStates();
        for(State s:acList){
            s.pushLambdaClosureState(startState);
            startState.pushLambdaClosureState(s);
        }
        return this;
    }
    
}
