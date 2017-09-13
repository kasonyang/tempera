package site.kason.tempera.parser;

import javax.annotation.Nullable;
import kalang.core.Type;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.lex.OffsetRange;
import site.kason.tempera.lexer.TexToken;
import site.kason.tempera.lexer.TexTokenType;

/**
 *
 * @author Kason Yang
 */
public class Exceptions {
    
    public static UnknownException unknownException(String message){
        return new UnknownException(message,null);
    }
    
    public static UnknownException unknownException(Throwable cause){
        return new UnknownException(cause);
    }
    
    public static SyntaxException unexpectedToken(TexToken token){
        return unexpectedToken(token, null);
    }
    
    public static SyntaxException unexpectedToken(TexToken token,@Nullable TexTokenType expectedTokenType){
        String msg = "unexcepted token:" + token.getTokenType().name();
        if(expectedTokenType!=null) msg += "," + expectedTokenType.name() + " is expected";
        return new SyntaxException(token.getOffset(),msg);
    }
    
    public static SemanticException classNotFound(String className){
        return new SemanticException(OffsetRange.NONE , "class not found:" + className);
    }
    
    public static SemanticException classNotFound(TexToken token,String className){
        return new SemanticException(token.getOffset(), "class not found:" + className);
    }
    
    public static SemanticException propertyNotFound(TexToken propertyToken){
        String property = propertyToken.getText();
        return new SemanticException(propertyToken.getOffset(),"property not found:" + property);
    }
    
    public static SemanticException varUndefinedException(TexToken token){
        String name = token.getText();
        return new SemanticException(token.getOffset(),name + " is undefined.");
    }
    
    public static SemanticException notIterableType(Type type,OffsetRange offset){
        return new SemanticException(offset , type + " is not interable.");
    }

}
