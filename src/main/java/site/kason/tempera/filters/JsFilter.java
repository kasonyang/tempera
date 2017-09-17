package site.kason.tempera.filters;

import org.apache.commons.text.StringEscapeUtils;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class JsFilter implements Filter {

  @Override
  public String filter(String value) {
    return StringEscapeUtils.escapeEcmaScript(value);
  }

}
