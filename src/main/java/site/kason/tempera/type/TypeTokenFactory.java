package site.kason.tempera.type;

import site.kason.tempera.lex.OffsetRange;
import site.kason.tempera.lex.TokenFactory;

/**
 *
 * @author Kason Yang
 */
public class TypeTokenFactory implements TokenFactory<TypeToken, TypeTokenInfo> {

    @Override
    public TypeToken createToken(TypeTokenInfo tokenInfo, 
            OffsetRange offset, int[] chars) {
        return new TypeToken(tokenInfo, offset.getStartOffset(), offset.getStopOffset(), new String(chars,0,chars.length));
    }

    @Override
    public TypeToken createEOIToken(OffsetRange offset) {
        return new TypeToken(TypeTokenInfo.EOI,offset.getStartOffset(),offset.getStopOffset(),"");
    }

}
