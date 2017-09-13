package site.kason.tempera.var;

import kalang.AstNotFoundException;
import site.kason.tempera.lex.LexException;
import static site.kason.tempera.var.VarTokenType.*;
import java.util.*;
import kalang.core.Type;
import site.kason.tempera.type.TypeParser;
/**
 *
 * @author Kason Yang
 */
public class VarParser {
    
    VarToken token;
    
    VarLexer lexer;
    
    private Map<String,Type> varNameToTypes = new HashMap();
    
    private RuntimeException unexpectedToken(VarTokenType type){
        return new RuntimeException("unexcepted token:" + type);
    }
    
    private VarToken expect(VarTokenType type) throws LexException{
        if(!token.getType().equals(type)){
            throw this.unexpectedToken(type);
        }
        VarToken tk = token;
        this.nextToken();
        return tk;
    }
    
    private void nextToken() throws LexException{
        this.token = lexer.nextToken();
    }
    
    private boolean isToken(VarTokenType type){
        if(token==null) return false;
        return token.getType().equals(type);
    }
    
    private void consume() throws LexException{
        this.nextToken();
    }
    
    private void varDecl() throws LexException, AstNotFoundException{
        expect(VAR);
        VarToken idToken = expect(IDENTITY);
        String varName = idToken.getText();
        expect(COLON);
        String varType;
        if(isToken(IDENTITY)){
            varType = token.getText();
            consume();
        }else if(isToken(TYPE)){
            varType = token.getText();
            consume();
        }else{
            throw new LexException(token.getOffset(), "unexpected token:" + token.getType().name());
        }
        this.varNameToTypes.put(varName, new TypeParser().parse(varType));
    }
    
    public void scan() throws LexException, AstNotFoundException{
        this.nextToken();
        while(isToken(VAR)){
            varDecl();
        }
        expect(VarTokenType.EOI);
    }
    
    public void parse(String input) throws AstNotFoundException, LexException{
        lexer = new VarLexer(input);
        this.scan();
    }

    public Map<String, Type> getDeclaredVars() {
        return varNameToTypes;
    }

}
