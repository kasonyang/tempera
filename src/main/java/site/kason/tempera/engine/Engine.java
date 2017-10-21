package site.kason.tempera.engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import kalang.AstNotFoundException;
import kalang.ast.ClassNode;
import kalang.compiler.AstLoader;
import kalang.compiler.JavaAstLoader;
import kalang.exception.Exceptions;
import site.kason.tempera.extension.Filter;
import site.kason.tempera.extension.Function;
import site.kason.tempera.model.RenderContext;
import site.kason.tempera.parser.ClassNameStrategy;
import site.kason.tempera.parser.DefaultClassNameStrategy;
import site.kason.tempera.parser.TemplateClassLoader;
import site.kason.tempera.parser.Renderer;
import site.kason.tempera.parser.TemplateParser;
import site.kason.tempera.source.StringTemplateSource;

/**
 *
 * @author Kason Yang
 */
public class Engine implements TemplateAstLoader {
  
  private static class CompileCache{
    Template template;
    //TODO rename to lastModified
    long sourceLastModified;
    ClassNode classNode;
  }

  @Nullable
  private final TemplateLoader templateLoader;
  
  /**
   * template name => CompileCache
   */
  private final Map<String,CompileCache> compileCacheMap = new HashMap();

  private final TemplateClassLoader templateClassLoader;

  private final AstLoader astLoader;
  
  private final RenderContext renderContext = new RenderContext();
  
  private final ClassNameStrategy templateClassNameStrategy;
  
  private final String leftDelimiter,rightDelimiter;

  public Engine() {
    this(Configuration.DEFAULT);
  }

  public Engine(Configuration conf) {
    this.templateLoader = conf.getTemplateLoader();
    ClassLoader classLoader = conf.getClassLoader();
    if (classLoader == null) {
      classLoader = Engine.class.getClassLoader();
    }
    this.astLoader = new JavaAstLoader(AstLoader.BASE_AST_LOADER, classLoader);
    String cachePath = conf.getCacheDir();
    File cacheDir = cachePath==null? null : new File(cachePath);
    this.templateClassLoader = new TemplateClassLoader(classLoader,cacheDir);
    Map<String, Filter> filters = conf.getFilters();
    for(Map.Entry<String, Filter> e:filters.entrySet()){
      renderContext.addFilter(e.getKey(), e.getValue());
    }
    for(Map.Entry<String, Function> e:conf.getFunctions().entrySet()){
      renderContext.addFunction(e.getKey(), e.getValue());
    }
    String defaultFilterName = conf.getDefaultFilter();
    if(defaultFilterName!=null && !defaultFilterName.isEmpty()){
      Filter defaultFilter = filters.get(defaultFilterName);
      Objects.requireNonNull(defaultFilter,"filter not found:"+defaultFilterName);
      renderContext.addDefaultFilter(defaultFilter);
    }
    this.leftDelimiter = conf.getLeftDelimiter();
    this.rightDelimiter = conf.getRightDelimiter();
    this.templateClassNameStrategy = conf.getClassNameStrategy();
  }

  public Template compile(TemplateSource source) throws IOException {
    String tplName = source.getName();
    String cacheKey = source.getCacheKey();
    long lastModified = source.lastModified();
    CompileCache compileCache = getCompiledCache(tplName, lastModified);
    if (compileCache==null) {
      String tplContent = source.getContent();
      String tplClassName = templateClassNameStrategy.generateClassName(tplName, tplContent, cacheKey);
      TemplateParser parser = new TemplateParser(tplClassName,tplName,tplContent,leftDelimiter,rightDelimiter, this, templateClassLoader);
      ClassNode ast = parser.getClassNode();
      compileCache = new CompileCache();
      compileCache.classNode = ast;
      compileCache.sourceLastModified = lastModified;
      this.compileCacheMap.put(tplName, compileCache);
      Class<Renderer> clazz = parser.parse();
      Template tpl = new DefaultTemplate(clazz,renderContext);
      compileCache.template = tpl;
    }
    return compileCache.template;
  }

  public Template compile(String templateName) throws IOException {
    if (templateLoader == null) {
      throw new RuntimeException("no template loader found.");
    }
    TemplateSource source = templateLoader.load(templateName);
    return compile(source);
  }

  public Template compileInline(String templateContent, String templateName, @Nullable String cacheKey) throws IOException {
    return compile(new StringTemplateSource(templateName, templateContent){
      
      @Override
      public String getCacheKey() {
        return cacheKey;
      }
      
    });
  }

  @Override
  public ClassNode loadTemplateAst(String templateName) throws IOException {
    //TODO check circle reference
    TemplateLoader tplLoader = this.templateLoader;
    if (tplLoader == null) {
      throw new TemplateNotFoundException(templateName);
    }
    TemplateSource source = tplLoader.load(templateName);
    long lastModified = source.lastModified();
    CompileCache compileCache = this.getCompiledCache(templateName,lastModified);
    if(compileCache==null){
      compile(templateName);
      compileCache = this.compileCacheMap.get(templateName);
      if(compileCache==null){
        throw Exceptions.unexceptedException("BUG:template not compiled:" + templateName);
      }
    }
    return compileCache.classNode;
  }

  @Override
  public ClassNode loadAst(String className) throws AstNotFoundException {
    return this.astLoader.loadAst(className);
  }
  
  private CompileCache getCompiledCache(String tplName, long lastModified) {
    CompileCache compiledTemplate = compileCacheMap.get(tplName);
    if (compiledTemplate != null && compiledTemplate.sourceLastModified == lastModified) {//cache is out of date
      return compiledTemplate;
    } else {
      return null;
    }
  }

}
