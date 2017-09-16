package site.kason.tempera.engine;

import java.io.Writer;
import java.util.Map;

/**
 *
 * @author Kason Yang
 */
public interface Template {
    
    public void render(Map<String,Object> data,Writer writer);
    
    public String render(Map<String,Object> data);

}
