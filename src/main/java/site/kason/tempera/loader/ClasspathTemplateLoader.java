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
  
  private String encoding = "utf-8";
  
  private String path = "/";

  public ClasspathTemplateLoader() {
    this(null);
  }

  public ClasspathTemplateLoader(@Nullable String[] suffixs) {
    if (suffixs == null || suffixs.length == 0) {
      suffixs = new String[]{""};
    }
    this.suffixs = suffixs;
  }

  public String getPath() {
    return path;
  }

  /**
   * Specify the path to search templates
   * @param path the path
   */
  public void setPath(String path) {
    path = path.replace('.', '/');
    if(!path.startsWith("/")){
      path = "/" + path;
    }
    if(!path.endsWith("/")){
      path += "/";
    }
    this.path = path;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  @Override
  public TemplateSource load(String templateName) throws TemplateNotFoundException {
    templateName = templateName.replace('.', '/');
    if(templateName.startsWith("/")){
      templateName = templateName.substring(1);
    }
    for (String s : suffixs) {
      String fullName = path + templateName.replace('.', '/') + s;
      InputStream is = ClasspathTemplateLoader.class.getResourceAsStream(fullName);
      if (is != null) {
        try {
          String content = InputStreamUtil.readAsString(is, encoding);
          return new StringTemplateSource(templateName, content);
        } catch (IOException ex) {
          throw new TemplateNotFoundException(ex);
        }
      }
    }
    throw new TemplateNotFoundException(templateName);
  }

}
