package site.kason.tempera.filters;

import java.util.Objects;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class RawFilter implements Filter {

  @Override
  public String filter(Object value) {
    return Objects.toString(value, "");
  }

}
