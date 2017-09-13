package site.kason.tempera.engine;

import java.util.Map;

/**
 *
 * @author Kason Yang
 */
public interface Template {
    
    public String render(Map<String,Object> data);

}
