package site.kason.tempera.engine;

/**
 *
 * @author Kason Yang
 */
public interface TemplateLoader {
    
    TemplateSource load(String templateName) throws TemplateNotFoundException;
    
}
