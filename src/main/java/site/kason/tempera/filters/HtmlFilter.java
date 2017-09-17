package site.kason.tempera.filters;

import org.apache.commons.lang3.StringEscapeUtils;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class HtmlFilter implements Filter {

  @Override
  public String filter(String value) {
    return StringEscapeUtils.escapeHtml4(value);
  }

}
