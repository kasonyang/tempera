package site.kason.tempera.functions;

import site.kason.tempera.extension.Function;

/**
 *
 * @author Kason Yang
 */
public class RightFunction implements Function {

  @Override
  public Object execute(Object[] arguments) {
    switch (arguments.length) {
      case 0:
        return "";
      case 1:
        return String.valueOf(arguments[0]);
      default:
        String val = String.valueOf(arguments[0]);
        Integer len = (Integer) arguments[1];
        int size = val.length();
        int offset = size - len;
        if(offset < 0){
          offset = 0;
        }
        return val.substring(offset, size);
    }
  }

}
