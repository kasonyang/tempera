package site.kason.tempera.functions;

import site.kason.tempera.extension.Function;

/**
 *
 * @author Kason Yang
 */
public class FormatFunction implements Function {

  @Override
  public Object execute(Object[] arguments) {
    if (arguments.length <= 0) {
      return "";
    }
    Object[] args = new Object[arguments.length - 1];
    if (args.length > 0) {
      System.arraycopy(arguments, 1, args, 0, args.length);
    }
    String fmt = String.valueOf(arguments[0]);
    return String.format(fmt, args);
  }
  
}
