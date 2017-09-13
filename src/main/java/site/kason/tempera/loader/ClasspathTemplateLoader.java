package site.kason.tempera.loader;

import site.kason.tempera.source.StringTemplateSource;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
import kamons.stream.InputStreamUtil;
import site.kason.tempera.engine.TemplateLoader;
import site.kason.tempera.engine.TemplateNotFoundException;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public class ClasspathTemplateLoader implements TemplateLoader {

  private final String[] suffixs;

  public ClasspathTemplateLoader() {
    this(new String[]{".tplx"});
  }

  public ClasspathTemplateLoader(@Nullable String[] suffixs) {
    if (suffixs == null || suffixs.length == 0) {
      suffixs = new String[]{""};
    }
    this.suffixs = suffixs;
  }

  @Override
  public TemplateSource load(String templateName) throws TemplateNotFoundException {
    for (String s : suffixs) {
      String fullName = '/' + templateName.replace('.', '/') + s;
      InputStream is = ClasspathTemplateLoader.class.getResourceAsStream(fullName);
      if (is != null) {
        try {
          String content = InputStreamUtil.readAsString(is, "utf-8");
          return new StringTemplateSource(templateName, content);
        } catch (IOException ex) {
          throw new TemplateNotFoundException(ex);
        }
      }
    }
    throw new TemplateNotFoundException(templateName);
  }

}
