package site.kason.tempera.engine;

import java.io.IOException;
import kalang.AstNotFoundException;
import kalang.ast.ClassNode;
import site.kason.klex.LexException;

/**
 *
 * @author Kason Yang
 */
public interface TemplateAstLoader {

  /**
   * load the class node of the template
   * @param templateName the name of template
   * @return the class node
   * @throws TemplateNotFoundException if template is not found
   * @throws LexException if lex exception occurs 
   * @throws IOException if i/o error occurs
   */
  public ClassNode loadTemplateAst(String templateName) throws TemplateNotFoundException, LexException, IOException;

  /**
   * load the class node by name
   * @param className the class name
   * @return the class node
   * @throws AstNotFoundException if class node is not found
   */
  public ClassNode loadAst(String className) throws AstNotFoundException;

}
