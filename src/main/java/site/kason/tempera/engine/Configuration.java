package site.kason.tempera.engine;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import site.kason.tempera.extension.Filter;
import site.kason.tempera.extension.Function;
import site.kason.tempera.filters.HtmlFilter;
import site.kason.tempera.filters.JsFilter;
import site.kason.tempera.filters.JsonFilter;
import site.kason.tempera.filters.LowerFilter;
import site.kason.tempera.filters.RawFilter;
import site.kason.tempera.filters.UpperFilter;
import site.kason.tempera.loader.ClasspathTemplateLoader;

/**
 *
 * @author Kason Yang
 */
public class Configuration {
  
  public final static Configuration DEFAULT;
  public final static Configuration DEFAULT_HTML;
  static{
    DEFAULT = new Configuration();
    DEFAULT.setTemplateLoader(new ClasspathTemplateLoader());
    DEFAULT.registerFilter("html", new HtmlFilter());
    DEFAULT.registerFilter("js", new JsFilter());
    DEFAULT.registerFilter("json", new JsonFilter());
    DEFAULT.registerFilter("lower", new LowerFilter());
    DEFAULT.registerFilter("raw", new RawFilter());
    DEFAULT.registerFilter("upper", new UpperFilter());
    DEFAULT_HTML = new Configuration(DEFAULT);
    DEFAULT_HTML.setDefaultFilter("html");
  }
  
  private String cacheDir;

  private ClassLoader classLoader;

  private TemplateLoader templateLoader;
  
  private Map<String,Function> functions = new HashMap();
  
  private Map<String,Filter> filters = new HashMap();
  
  private String defaultFilter = "";
  
  private String leftDelimiter = "{{";
  
  private String rightDelimiter = "}}";

  public Configuration() {
  }

  public Configuration(Configuration config) {
    this.cacheDir = config.getCacheDir();
    this.classLoader = config.getClassLoader();
    this.templateLoader = config.getTemplateLoader();
    this.filters.putAll(config.getFilters());
    this.functions.putAll(config.getFunctions());
    this.defaultFilter = config.getDefaultFilter();
  }  
  
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
  
  public void registerFilter(String name,Filter filter){
    filters.put(name, filter);
  }
  
  public Map<String,Filter> getFilters(){
    return this.filters;
  }
  
  public void registerFunction(String name,Function function){
    this.functions.put(name, function);
  }
  
  public Map<String,Function> getFunctions(){
    return this.functions;
  }
  
  public String getDefaultFilter() {
    return defaultFilter;
  }

  public void setDefaultFilter(String defaultFilter) {
    this.defaultFilter = defaultFilter;
  }

  public String getLeftDelimiter() {
    return leftDelimiter;
  }

  public void setLeftDelimiter(String leftDelimiter) {
    this.leftDelimiter = leftDelimiter;
  }

  public String getRightDelimiter() {
    return rightDelimiter;
  }

  public void setRightDelimiter(String rightDelimiter) {
    this.rightDelimiter = rightDelimiter;
  }

}
