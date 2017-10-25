package site.kason.tempera.html;

import java.util.Objects;
import org.apache.commons.text.StringEscapeUtils;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class JsFilter implements Filter {

  @Override
  public String filter(Object value) {
    return StringEscapeUtils.escapeEcmaScript(Objects.toString(value,""));
  }

}
