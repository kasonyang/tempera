package site.kason.tempera.filters;

import org.apache.commons.text.StringEscapeUtils;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class JsonFilter implements Filter {

  @Override
  public String filter(String value) {
    return StringEscapeUtils.escapeJson(value);
  }

}
