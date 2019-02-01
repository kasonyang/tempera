package site.kason.tempera.engine;

import kalang.compiler.AstNotFoundException;
import kalang.compiler.ast.ClassNode;
import kalang.compiler.compile.AstLoader;
import kalang.compiler.compile.JavaAstLoader;
import kalang.compiler.exception.Exceptions;
import site.kason.tempera.extension.Filter;
import site.kason.tempera.extension.Function;
import site.kason.tempera.model.RenderContext;
import site.kason.tempera.parser.ClassNameStrategy;
import site.kason.tempera.parser.Renderer;
import site.kason.tempera.parser.TemplateClassLoader;
import site.kason.tempera.parser.TemplateParser;
import site.kason.tempera.source.StringTemplateSource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kason Yang
 */
public class Engine implements TemplateAstLoader {

    private static class CompileCache {

        Template template;
        long sourceLastModified;
        ClassNode classNode;
    }

    private final TemplateLoader[] templateLoaders;

    /**
     * template name => CompileCache
     */
    private final Map<String, CompileCache> compiledCacheMap = new HashMap();

    private final Map<String, CompileCache> compilingCacheMap = new HashMap();

    private final TemplateClassLoader templateClassLoader;

    private final AstLoader astLoader;

    private final RenderContext renderContext = new RenderContext();

    private final ClassNameStrategy templateClassNameStrategy;

    private final String leftDelimiter, rightDelimiter;

    public Engine() {
        this(Configuration.DEFAULT);
    }

    public Engine(Configuration conf) {
        this.templateLoaders = conf.getTemplateLoaders();
        ClassLoader classLoader = conf.getClassLoader();
        if (classLoader == null) {
            classLoader = Engine.class.getClassLoader();
        }
        this.astLoader = new JavaAstLoader(AstLoader.BASE_AST_LOADER, classLoader);
        String cachePath = conf.getCacheDir();
        File cacheDir = cachePath == null ? null : new File(cachePath);
        this.templateClassLoader = new TemplateClassLoader(classLoader, cacheDir);
        Map<String, Filter> filters = conf.getFilters();
        for (Map.Entry<String, Filter> e : filters.entrySet()) {
            renderContext.addFilter(e.getKey(), e.getValue());
        }
        for (Map.Entry<String, Function> e : conf.getFunctions().entrySet()) {
            renderContext.addFunction(e.getKey(), e.getValue());
        }
        renderContext.setEscapeHandler(conf.getEscapeHandler());
        this.leftDelimiter = conf.getLeftDelimiter();
        this.rightDelimiter = conf.getRightDelimiter();
        this.templateClassNameStrategy = conf.getClassNameStrategy();
    }

    /**
     * Compile a source to template.This method is thread-safe.
     *
     * @param source the template source to compile
     * @return the compiled template
     * @throws IOException
     */
    public Template compile(TemplateSource source) throws IOException {
        String tplName = source.getName();
        long lastModified = source.lastModified();
        CompileCache compileCache = getCompiledCache(tplName, lastModified);
        if (compileCache != null) {
            return compileCache.template;
        }
        synchronized (compilingCacheMap) {
            compileCache = getCompiledCache(tplName, lastModified);
            if (compileCache == null) {
                String tplContent = source.getContent();
                String tplClassName = templateClassNameStrategy.generateClassName(source);
                TemplateParser parser = new TemplateParser(tplClassName, tplName, tplContent, leftDelimiter, rightDelimiter, this, templateClassLoader);
                ClassNode ast = parser.getClassNode();
                compileCache = new CompileCache();
                compileCache.classNode = ast;
                compileCache.sourceLastModified = lastModified;
                compilingCacheMap.put(tplName, compileCache);
                Class<Renderer> clazz = parser.parse();
                Template tpl = new DefaultTemplate(clazz, renderContext);
                compileCache.template = tpl;
                compilingCacheMap.remove(tplName);
                compiledCacheMap.put(tplName, compileCache);
            }
            return compileCache.template;
        }
    }

    /**
     * Compile a template by name.This method is thread-safe.
     *
     * @param templateName the name of template
     * @return the compiled template
     * @throws IOException
     */
    public Template compile(String templateName) throws IOException {
        return compile(loadTemplate(templateName));
    }

    /**
     * Compile a string to template.This method is thread-safe.
     *
     * @param templateContent the template's content
     * @param templateName    the template's name
     * @return the compiled template
     * @throws IOException
     */
    public Template compileInline(String templateContent, String templateName) throws IOException {
        return compile(new StringTemplateSource(templateName, templateContent));
    }

    @Override
    public ClassNode loadTemplateAst(String templateName) throws IOException {
        //TODO check circle reference
        TemplateSource source = loadTemplate(templateName);
        long lastModified = source.lastModified();
        CompileCache compileCache = this.getCompiledCache(templateName, lastModified);
        if (compileCache == null) {
            compileCache = this.compilingCacheMap.get(templateName);
        }
        if (compileCache == null) {
            compile(templateName);
            compileCache = this.compiledCacheMap.get(templateName);
            if (compileCache == null) {
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
        CompileCache compiledTemplate = compiledCacheMap.get(tplName);
        if (compiledTemplate != null && compiledTemplate.sourceLastModified == lastModified) {//cache is out of date
            return compiledTemplate;
        } else {
            return null;
        }
    }

    private TemplateSource loadTemplate(String templateName) throws TemplateNotFoundException {
        for (TemplateLoader tl : templateLoaders) {
            try {
                TemplateSource source = tl.load(templateName);
                return source;
            } catch (TemplateNotFoundException ex) {

            }
        }
        throw new TemplateNotFoundException("template not found:" + templateName);
    }

}
