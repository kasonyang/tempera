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
   */
  String getContent();

  /**
   * Get the path of template. Just for the convenience of debugging.
   *
   * @return
   */
  String getPath();
  
  long lastModified();

}
