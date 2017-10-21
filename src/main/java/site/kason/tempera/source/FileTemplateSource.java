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

  public FileTemplateSource(String name,File file, String encoding) {
    this.file = file;
    this.encoding = encoding;
    this.name = name;
  }

  @Override
  public String getContent() throws IOException {
    return FileUtils.readFileToString(file, encoding);
  }

  @Override
  public String getCacheKey() {
    try {
      return file.getCanonicalPath() + file.lastModified();
    } catch (IOException ex) {
      return null;
    }
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
