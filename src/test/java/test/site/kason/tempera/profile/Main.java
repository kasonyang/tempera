package test.site.kason.tempera.profile;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import site.kason.tempera.engine.Configuration;
import site.kason.tempera.engine.Engine;
import site.kason.tempera.engine.Template;
import site.kason.tempera.loader.ClasspathTemplateLoader;

/**
 *
 * @author Kason Yang
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String[] names = new String[]{"A", "B", "C"};
        List<String> list = Arrays.asList(names);
        ClasspathTemplateLoader templateLoader = new ClasspathTemplateLoader(new String[]{".tplx"});
        templateLoader.setPath("/templates/");
        Configuration conf = new Configuration(Configuration.DEFAULT);
        conf.setTemplateLoader(templateLoader);
        Engine engine = new Engine(conf);
        Template tpl = engine.compile("/main");
        tpl.render(Collections.singletonMap("names", list), new StringWriter());
    }

}
