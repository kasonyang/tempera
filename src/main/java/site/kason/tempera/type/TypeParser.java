package site.kason.tempera.type;

import java.util.LinkedList;
import java.util.List;
import kalang.AstNotFoundException;
import kalang.core.Type;
import kalang.core.Types;
import site.kason.tempera.lex.LexException;
import static site.kason.tempera.type.TypeTokenInfo.*;

/**
 *
 * @author Kason Yang
 */
public class TypeParser {
    
    TypeToken token;
    
    TypeLexer lexer;
    
    private RuntimeException unexpectedToken(TypeTokenInfo type){
        return new RuntimeException("unexcepted token:" + type);
    }
    
    private TypeToken expect(TypeTokenInfo type) throws LexException{
        if(!token.getType().equals(type)){
            throw this.unexpectedToken(type);
        }
        TypeToken tk = token;
        this.nextToken();
        return tk;
    }
    
    private void nextToken() throws LexException{
        this.token = lexer.nextToken();
    }
    
    private boolean isToken(TypeTokenInfo type){
        if(token==null) return false;
        return token.getType().equals(type);
    }
    
    private void consume() throws LexException{
        this.nextToken();
    }
    
    private Type scanType() throws AstNotFoundException, LexException{
        TypeToken tk = expect(NAME);
        Type type;
        if(isToken(LEFT_DELIMIT)){
            List<Type> pms = new LinkedList();
            do{
                consume();
                pms.add(scanType());
            }while(isToken(COMMA));
            expect(RIGHT_DELIMIT);
            type = Types.getClassType(Types.getClassType(tk.getText()).getClassNode(), pms.toArray(new Type[pms.size()]));
        }else{
            type = Types.getClassType(tk.getText());
        }
        while(isToken(LBRACK)){
            consume();
            expect(RBRACK);
            type = Types.getArrayType(type);
        }
        return type;
    }
    
    public Type parse(String type) throws AstNotFoundException, LexException{
        lexer = new TypeLexer(type);
        this.nextToken();
        Type t = this.scanType();
        expect(TypeTokenInfo.EOI);
        return t;
    }

}
