package site.kason.tempera.util;

import kalang.compiler.ast.ExprNode;
import site.kason.klex.OffsetRange;

/**
 *
 * @author Kason Yang
 */
public class OffsetUtil {
    
    public static OffsetRange getOffsetOfExprNode(ExprNode expr){
        kalang.compiler.compile.OffsetRange os = expr.offset;
        return new OffsetRange(os.startOffset, os.stopOffset, os.startLine, os.startLineColumn, os.stopLine, os.stopLineColumn);
    }

}
