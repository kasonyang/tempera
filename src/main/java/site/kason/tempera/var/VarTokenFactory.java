package site.kason.tempera.var;

import site.kason.tempera.lex.OffsetRange;
import site.kason.tempera.lex.TokenFactory;

/**
 *
 * @author Kason Yang
 */
public class VarTokenFactory implements TokenFactory<VarToken, VarTokenType> {

    @Override
    public VarToken createToken(VarTokenType tokenInfo,
            OffsetRange offset, int[] chars) {
        return new VarToken(tokenInfo, offset, new String(chars, 0, chars.length));
    }

    @Override
    public VarToken createEOIToken(OffsetRange offset) {
        return new VarToken(VarTokenType.EOI, offset, "");
    }

}
