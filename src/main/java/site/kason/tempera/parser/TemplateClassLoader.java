package site.kason.tempera.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/**
 *
 * @author Kason Yang
 */
public class TemplateClassLoader extends ClassLoader{
    
    private final File cacheDir;

    public TemplateClassLoader(@Nullable ClassLoader parent,@Nullable File cacheDir) {
        super(parent);
        this.cacheDir = cacheDir;
    }

    public TemplateClassLoader() {
        this(TemplateClassLoader.class.getClassLoader(),null);
    }
    
    public Class generateTemplateClass(String name,byte[] bytes){
        if(cacheDir!=null){
            String fileName = name.replace(".", "/") + ".class";
            try {
                try (FileOutputStream os = new FileOutputStream(new File(cacheDir.getAbsolutePath(),fileName))) {
                    os.write(bytes);
                }
            } catch (IOException ex) {
                Logger.getLogger(TemplateClassLoader.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        return this.defineClass(name, bytes,0,bytes.length);
    }
    
}
