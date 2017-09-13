package site.kason.tempera.lex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import site.kason.tempera.lex.nfa.MatchedResult;
import site.kason.tempera.lex.nfa.NFA;
import site.kason.tempera.lex.nfa.State;

/**
 *
 * @author Kason Yang
 */
public class Lexer<TOKEN,TOKEN_INFO extends TokenInfo> {
    
    private NFA nfa;
    private Map<State,TOKEN_INFO> stateToTokenInfo = new HashMap();
    private CharStream charBuffer;
    //private int offset = 0;

    private TokenFactory<TOKEN,TOKEN_INFO> tokenFactory;
    
    public Lexer(CharStream charBuffer,TOKEN_INFO[] tokenInfos,TokenFactory<TOKEN,TOKEN_INFO> tokenFactory) {
        this.charBuffer = charBuffer;
        for(int i=0;i<tokenInfos.length;i++){
            this.addTokenRule(tokenInfos[i]);
        }
        this.tokenFactory = tokenFactory;
    }
    
    private void addTokenRule(TOKEN_INFO info){
        NFA theNfa = info.getNFA();
        if(theNfa!=null){
            for(State s:info.getNFA().getAcceptedStates()){
                stateToTokenInfo.put(s, info);
            }
            if(this.nfa==null){
                nfa = info.getNFA();
            }else{
                nfa.or(info.getNFA());
            }
        }
    }
    
    /**
     * select the best token by token value
     * @param states
     * @return 
     */
    private TOKEN_INFO selectBestToken(State[] states){
        TOKEN_INFO bestTokenInfo = null;
        for(State s:states){
            TOKEN_INFO tk = this.stateToTokenInfo.get(s);
            if(bestTokenInfo==null ||  tk.getPriority() < bestTokenInfo.getPriority()){
                bestTokenInfo = tk;
            }
        }
        return bestTokenInfo;
    }
    
    private boolean hasNextToken(){
        return charBuffer.lookAhead(1) != CharStream.EOF;
    }
    
    public TOKEN nextToken() throws LexException{
        if(!this.hasNextToken()){   
            return this.tokenFactory.createEOIToken(
                    new OffsetRange(
                            charBuffer.getCurrentOffset(), charBuffer.getCurrentOffset()+1
                            , charBuffer.getCurrentLine(), charBuffer.getCurrentColumn()
                            , charBuffer.getCurrentLine(), charBuffer.getCurrentColumn()+1
                    )
            );
        }
        int startOffset = charBuffer.getCurrentOffset();
        int startLine = charBuffer.getCurrentLine();
        int startColumn = charBuffer.getCurrentColumn();
        MatchedResult match = nfa.match(charBuffer);
        if(match==null){
            throw new LexException(
                    new OffsetRange(startOffset, startOffset, startLine, startColumn, startLine, startColumn),
                    "unexcepted input");
        }
        State[] matchedStates = match.getMatchedState();
        TOKEN_INFO tokenType = this.selectBestToken(matchedStates);
        int stopOffset = charBuffer.getCurrentOffset()-1;
        int stopLine = charBuffer.getCurrentLine();
        int stopColumn = charBuffer.getCurrentColumn()-1;
        TOKEN tk = this.tokenFactory.createToken(tokenType, 
                new OffsetRange(startOffset, stopOffset, startLine, startColumn, stopLine, stopColumn)
                ,match.getMatchedChars());
        return tk;
    }
    
    public List<TOKEN> nextTokens() throws LexException{
        LinkedList<TOKEN> resultTokens = new LinkedList();
        TOKEN token;
        while(this.hasNextToken()){
            token = this.nextToken();
            resultTokens.add(token);
        }
        return resultTokens;
    }
    
    public int getRecognizedLength(){
        return charBuffer.getCurrentOffset();
    }
    
    public void skip(int count){
        this.charBuffer.consume(count);
    }
    
    public int getCaret(){
        return this.charBuffer.getCurrentOffset();
    }

}
