package test.site.kason.tempera.parser;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import test.site.kason.tempera.TestBase;

/**
 *
 * @author Kason Yang
 */
public class ExtensionTest extends TestBase {
  
  public ExtensionTest() {
  }
  
  public void test() throws IOException{
    assertRender("he", "{{left(\"hello\",2)}}");
    assertRender("hello", "{{left(\"hello\",100)}}");
    
    assertRender("lo", "{{right(\"hello\",2)}}");
    assertRender("hello", "{{right(\"hello\",100)}}");
    
  }
  
}
