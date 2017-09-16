package site.kason.tempera.engine;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import site.kason.tempera.extension.Function;
import site.kason.tempera.loader.ClasspathTemplateLoader;

/**
 *
 * @author Kason Yang
 */
public class Configuration {
  
  public final static Configuration DEFAULT = new Configuration();
  static{
    DEFAULT.setTemplateLoader(new ClasspathTemplateLoader());
  }

  private String cacheDir;

  private ClassLoader classLoader;

  private TemplateLoader templateLoader;
  
  @Nullable
  public String getCacheDir() {
    return cacheDir;
  }

  public void setCacheDir(String cacheDir) {
    this.cacheDir = cacheDir;
  }

  @Nullable
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  public void setClassLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  @Nullable
  public TemplateLoader getTemplateLoader() {
    return templateLoader;
  }

  public void setTemplateLoader(TemplateLoader templateLoader) {
    this.templateLoader = templateLoader;
  }

}
