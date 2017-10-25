package site.kason.tempera.html;

import java.util.Objects;
import org.apache.commons.text.StringEscapeUtils;
import site.kason.tempera.engine.EscapeHandler;

/**
 *
 * @author Kason Yang
 */
public class HtmlEscapeHandler implements EscapeHandler {

  @Override
  public String escape(String value) {
    return StringEscapeUtils.escapeHtml4(Objects.toString(value, ""));
  }

}
