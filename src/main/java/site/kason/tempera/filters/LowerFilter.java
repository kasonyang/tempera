package site.kason.tempera.filters;

import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class LowerFilter implements Filter {

  @Override
  public String filter(Object value) {
    String str = String.valueOf(value);
    return str==null ? "null" : str.toLowerCase();
  }

}
