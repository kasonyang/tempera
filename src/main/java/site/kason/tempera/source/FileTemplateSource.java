package site.kason.tempera.source;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public class FileTemplateSource implements TemplateSource {

  File file;
  String name;
  private final String encoding;
  private final String content;

  public FileTemplateSource(String name,File file, String encoding) throws IOException {
    this.file = file;
    this.encoding = encoding;
    this.name = name;
    this.content = FileUtils.readFileToString(file, encoding);
  }

  @Override
  public String getContent(){
    return content;
  }

  @Override
  public String getPath() {
    try {
      return file.getCanonicalPath();
    } catch (IOException ex) {
      return file.getAbsolutePath();
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public long lastModified() {
    return this.file.lastModified();
  }

}
