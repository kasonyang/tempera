package site.kason.tempera.filters;

import java.util.Objects;
import org.apache.commons.text.StringEscapeUtils;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class JsonFilter implements Filter {

  @Override
  public String filter(Object value) {
    return StringEscapeUtils.escapeJson(Objects.toString(value,""));
  }

}
