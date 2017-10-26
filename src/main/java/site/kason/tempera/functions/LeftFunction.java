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
        String str = String.valueOf(arguments[0]);
        Number lenArg = (Number) arguments[1];
        int len = lenArg.intValue();
        if(len>str.length()){
          len = str.length();
        }
        return str.substring(0, len);
    }
  }

}
