package site.kason.tempera.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class RenderContext {

  private final List<Filter> defaultFilters = new LinkedList();
  
  private final Map<String,Filter> filters = new HashMap();
  

  public Filter[] getDefaultFilters() {
    return defaultFilters.toArray(new Filter[0]);
  }

  public void addDefaultFilter(Filter defaultFilter) {
    this.defaultFilters.add(defaultFilter);
  }

  public void addFilter(String name,Filter filter) {
    this.filters.put(name, filter);
  }
  
  public Filter getFilter(String name){
    return filters.get(name);
  }

}
