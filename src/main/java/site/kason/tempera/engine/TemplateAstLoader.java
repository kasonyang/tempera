package site.kason.tempera.engine;

import java.io.IOException;
import kalang.AstNotFoundException;
import kalang.ast.ClassNode;
import site.kason.tempera.lex.LexException;

/**
 *
 * @author Kason Yang
 */
public interface TemplateAstLoader {
    
    public ClassNode loadTemplateAst(String templateName) throws TemplateNotFoundException,LexException,IOException;
    
    public ClassNode loadAst(String className) throws AstNotFoundException;

}
