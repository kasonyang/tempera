package site.kason.tempera.engine;

import java.io.IOException;
import javax.annotation.Nullable;

/**
 *
 * @author Kason Yang
 */
public interface TemplateSource {

  String getName();

  /**
   * get the content of template
   *
   * @return
   * @throws IOException
   */
  String getContent();

  /**
   * get the key for cache
   *
   * @return the key for cache,return null if can not be cache.
   */
  //TODO remove
  @Nullable
  String getCacheKey();

  /**
   * Get the path of template. Just for the convenience of debugging.
   *
   * @return
   */
  String getPath();
  
  long lastModified();

}
