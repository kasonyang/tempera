package site.kason.tempera.model;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class RenderContext {

  private final List<Filter> defaultFilters = new LinkedList();

  public Filter[] getDefaultFilters() {
    return defaultFilters.toArray(new Filter[0]);
  }

  public void addDefaultFilter(Filter defaultFilter) {
    this.defaultFilters.add(defaultFilter);
  }

}
