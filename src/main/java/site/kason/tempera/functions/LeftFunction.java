package site.kason.tempera.functions;

import site.kason.tempera.extension.Function;

/**
 *
 * @author Kason Yang
 */
public class LeftFunction implements Function {

  @Override
  public Object execute(Object[] arguments) {
    switch (arguments.length) {
      case 0:
        return "";
      case 1:
        return String.valueOf(arguments[0]);
      default:
        Integer len = (Integer) arguments[1];
        return String.valueOf(arguments[0]).substring(0, len);
    }
  }

}
