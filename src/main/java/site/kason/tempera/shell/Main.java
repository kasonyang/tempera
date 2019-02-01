package site.kason.tempera.shell;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import kamons.console.StatusBar;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import site.kason.tempera.engine.Configuration;
import site.kason.tempera.engine.TemplateLoader;
import site.kason.tempera.engine.Engine;
import site.kason.tempera.loader.FileTemplateLoader;

/**
 *
 * @author Kason Yang
 */
public class Main {

  private final static String APP_NAME = "templatex";

  private final static Options OPTIONS;

  static {
    OPTIONS = new Options()
            .addOption("h", false, "show this help message")
            .addOption("o",true,"specifies output directory")
            .addOption("s",true,"specifies source directory")
            .addOption("cp","specifies class path")
    ; 
  }

  public static void printUsage() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(APP_NAME, OPTIONS);
  }

  public static void main(String[] args) throws ParseException, IOException, Exception {
    DefaultParser parser = new DefaultParser();
    CommandLine cli = parser.parse(OPTIONS, args);
    if (cli.hasOption("h")) {
      printUsage();
    } else {
      run(cli);
    }
  }

  private static void run(CommandLine cl) throws IOException {
    //String[] args = cl.getArgs();
    String outPath = cl.getOptionValue("o", ".");
    String srcPath = cl.getOptionValue("s",".");
    String classPath = cl.getOptionValue("cp", ".");
    Configuration conf = new Configuration(Configuration.DEFAULT);
    conf.setCacheDir(outPath);
    FileTemplateLoader tloader = new FileTemplateLoader(new File(srcPath),new String[]{".tplx"},"utf-8");
    conf.registerTemplateLoader(tloader);
    try{
      conf.setClassLoader(new URLClassLoader(new URL[]{new File(classPath).toURI().toURL()}));
    }catch(MalformedURLException ex){
      throw new RuntimeException(ex);
    }
    Engine engine = new Engine(conf);
    String[] tplNames = tloader.listTemplateNames();
    for(String n:tplNames){
      engine.compile(n);
    }
  }

}
