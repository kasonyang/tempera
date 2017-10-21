package site.kason.tempera.loader;

import site.kason.tempera.source.FileTemplateSource;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import kalang.util.ClassNameUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import site.kason.tempera.engine.TemplateLoader;
import site.kason.tempera.engine.TemplateNotFoundException;
import site.kason.tempera.engine.TemplateSource;

/**
 *
 * @author Kason Yang
 */
public class FileTemplateLoader implements TemplateLoader {

  private final String[] suffix;

  private final String baseDir;

  private final String encoding;

  public final static String DEFAULT_ENCODING = "utf-8";

  public FileTemplateLoader(String baseDir, String[] suffix, String encoding) {
    this.suffix = suffix;
    this.baseDir = baseDir;
    this.encoding = encoding;
  }

  public FileTemplateLoader(String baseDir) {
    this(baseDir, new String[0], DEFAULT_ENCODING);
  }

  @Override
  public TemplateSource load(String templateName) throws TemplateNotFoundException {
    String[] sfx = suffix;
    if (sfx == null || sfx.length == 0) {
      sfx = new String[]{""};
    }
    for (String s : sfx) {
      File file = new File(this.baseDir, templateName.replace('.', '/') + s);
      if (file.exists()) {
        try{
          return new FileTemplateSource(templateName , file , this.encoding);
        }catch(IOException ex){
          throw new TemplateNotFoundException("unable to access file:" + file);
        }
      }
    }
    throw new TemplateNotFoundException("template not found:" + templateName);
  }

  public String[] listTemplateNames() {
    File bd = new File(this.baseDir);
    List<String> names = new LinkedList();
    searchTemplateNames("", bd, names);
    return names.toArray(new String[names.size()]);
  }

  private void searchTemplateNames(String namePrefix, File dir, Collection<String> results) {
    File[] files = dir.listFiles();
    for (File f : files) {
      if (f.isFile()) {
        String fn = f.getName();
        for (String sf : this.suffix) {
          if (fn.endsWith(sf)) {
            results.add(namePrefix + fn.substring(0, fn.length() - sf.length()));
            break;
          }
        }
      } else if (f.isDirectory()) {
        searchTemplateNames(namePrefix + f.getName() + ".", f, results);
      }
    }
  }

}
