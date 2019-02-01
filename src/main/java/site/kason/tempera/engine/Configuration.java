package site.kason.tempera.engine;

import site.kason.tempera.extension.Filter;
import site.kason.tempera.extension.Function;
import site.kason.tempera.filters.LowerFilter;
import site.kason.tempera.filters.UpperFilter;
import site.kason.tempera.functions.FormatFunction;
import site.kason.tempera.functions.LeftFunction;
import site.kason.tempera.functions.RightFunction;
import site.kason.tempera.html.HtmlEscapeHandler;
import site.kason.tempera.html.JsFilter;
import site.kason.tempera.html.JsonFilter;
import site.kason.tempera.loader.ClasspathTemplateLoader;
import site.kason.tempera.loader.FileTemplateLoader;
import site.kason.tempera.parser.ClassNameStrategy;
import site.kason.tempera.parser.DefaultClassNameStrategy;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Kason Yang
 */
public class Configuration {

    private final static ClassNameStrategy DEFAULT_CLASS_NAME_STRATEGY = new DefaultClassNameStrategy();

    public final static Configuration DEFAULT;
    public final static Configuration DEFAULT_HTML;

    static {
        DEFAULT = new Configuration();
        DEFAULT.registerFilter("lower", new LowerFilter());
        DEFAULT.registerFilter("upper", new UpperFilter());
        DEFAULT.registerFunction("format", new FormatFunction());
        DEFAULT.registerFunction("left", new LeftFunction());
        DEFAULT.registerFunction("right", new RightFunction());

        DEFAULT_HTML = new Configuration(DEFAULT);
        DEFAULT_HTML.registerFilter("js", new JsFilter());
        DEFAULT_HTML.registerFilter("json", new JsonFilter());
        DEFAULT_HTML.setEscapeHandler(new HtmlEscapeHandler());
    }

    private String cacheDir;

    private ClassLoader classLoader;

    private List<TemplateLoader> templateLoaders = new LinkedList();

    private Map<String, Function> functions = new HashMap();

    private Map<String, Filter> filters = new HashMap();

    private EscapeHandler escapeHandler;

    private String leftDelimiter = "{{";

    private String rightDelimiter = "}}";

    private ClassNameStrategy classNameStrategy = DEFAULT_CLASS_NAME_STRATEGY;

    public Configuration() {
    }

    public Configuration(Configuration config) {
        this.cacheDir = config.getCacheDir();
        this.classLoader = config.getClassLoader();
        for (TemplateLoader t : config.getTemplateLoaders()) {
            registerTemplateLoader(t);
        }
        this.filters.putAll(config.getFilters());
        this.functions.putAll(config.getFunctions());
        this.escapeHandler = config.getEscapeHandler();
        this.classNameStrategy = config.getClassNameStrategy();
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

    public TemplateLoader[] getTemplateLoaders() {
        return templateLoaders.toArray(new TemplateLoader[0]);
    }

    public void registerTemplateLoader(TemplateLoader templateLoader) {
        this.templateLoaders.add(templateLoader);
    }

    public void registerClasspathTemplateLoader(@Nullable String[] suffix) {
        registerTemplateLoader(new ClasspathTemplateLoader(suffix));
    }

    public void registerFileTemplateLoader(File baseDir, String[] suffix, String encoding) {
        registerTemplateLoader(new FileTemplateLoader(baseDir,suffix,encoding));
    }

    public void registerFileTemplateLoader(File baseDir) {
        registerFileTemplateLoader(baseDir,new String[0],"utf-8");
    }

    public void registerFilter(String name, Filter filter) {
        filters.put(name, filter);
    }

    public Map<String, Filter> getFilters() {
        return this.filters;
    }

    public void registerFunction(String name, Function function) {
        this.functions.put(name, function);
    }

    public Map<String, Function> getFunctions() {
        return this.functions;
    }

    @Nullable
    public EscapeHandler getEscapeHandler() {
        return escapeHandler;
    }

    public void setEscapeHandler(EscapeHandler escapeHandler) {
        this.escapeHandler = escapeHandler;
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

    public ClassNameStrategy getClassNameStrategy() {
        return classNameStrategy;
    }

    public void setClassNameStrategy(ClassNameStrategy classNameStrategy) {
        this.classNameStrategy = classNameStrategy;
    }

}
