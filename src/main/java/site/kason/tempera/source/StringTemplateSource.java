package site.kason.tempera.source;

import java.io.IOException;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public class StringTemplateSource implements TemplateSource {

  private final String content;
  private final String sourceName;

  /**
   *
   * @param sourceName the name use as cache key and path
   * @param content the content of template
   */
  public StringTemplateSource(String sourceName, String content) {
    this.content = content;
    this.sourceName = sourceName;
  }

  @Override
  public String getContent() throws IOException {
    return content;
  }

  @Override
  public String getCacheKey() {
    return this.sourceName;
  }

  @Override
  public String getPath() {
    return this.sourceName;
  }

  @Override
  public String getName() {
    return this.sourceName;
  }

}
