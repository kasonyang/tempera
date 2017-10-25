package site.kason.tempera.extension;

/**
 *
 * @author Kason Yang
 */
public interface Function {

  /**
   * Execute the function
   * @param arguments the arguments passed
   * @return the result of function
   */
  Object execute(Object[] arguments);

}
