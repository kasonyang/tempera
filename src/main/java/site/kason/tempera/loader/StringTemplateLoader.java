package site.kason.tempera.loader;

import site.kason.tempera.source.StringTemplateSource;
import java.util.HashMap;
import java.util.Map;
import site.kason.tempera.engine.TemplateLoader;
import site.kason.tempera.engine.TemplateNotFoundException;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public class StringTemplateLoader implements TemplateLoader{
    
    private Map<String,TemplateSource> templateSources = new HashMap();
    
    public void addSource(String templateName,String content){
        this.templateSources.put(templateName, new StringTemplateSource(templateName,content));
    }

    @Override
    public TemplateSource load(String templateName) throws TemplateNotFoundException {
        TemplateSource source = this.templateSources.get(templateName);
        if(source==null) throw new TemplateNotFoundException(templateName);
        return source;
    }

}
