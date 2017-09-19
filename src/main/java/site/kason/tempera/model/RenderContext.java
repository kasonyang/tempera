package site.kason.tempera.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import site.kason.tempera.extension.Filter;
import site.kason.tempera.extension.Function;

/**
 *
 * @author Kason Yang
 */
public class RenderContext {

  private final List<Filter> defaultFilters = new LinkedList();
  
  private final Map<String,Filter> filters = new HashMap();
  
  private final Map<String,Function> functions = new HashMap();
  

  public Filter[] getDefaultFilters() {
    return defaultFilters.toArray(new Filter[0]);
  }

  public void addDefaultFilter(Filter defaultFilter) {
    this.defaultFilters.add(defaultFilter);
  }

  public void addFilter(String name,Filter filter) {
    this.filters.put(name, filter);
  }
  
  @Nullable
  public Filter getFilter(String name){
    return filters.get(name);
  }
  
  public void addFunction(String name,Function function){
    this.functions.put(name, function);
  }
  
  @Nullable
  public Function getFunction(String name){
    return this.functions.get(name);
  }

}
