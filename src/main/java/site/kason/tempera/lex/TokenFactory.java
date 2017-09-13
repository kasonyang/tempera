package site.kason.tempera.lex;
/**
 *
 * @author Kason Yang
 */
public interface TokenFactory<TOKEN,TOKEN_INFO> {
    
     TOKEN createToken(TOKEN_INFO tokenInfo
             ,OffsetRange offset
             ,int[] chars);
     
     TOKEN createEOIToken(OffsetRange offset);

}
