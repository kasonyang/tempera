package site.kason.tempera.model;

import javax.annotation.Nullable;
import site.kason.tempera.extension.Filter;

/**
 *
 * @author Kason Yang
 */
public class RenderContext {

  private Filter defaultFilter;

  @Nullable
  public Filter getDefaultFilter() {
    return defaultFilter;
  }

  public void setDefaultFilter(Filter defaultFilter) {
    this.defaultFilter = defaultFilter;
  }

}
