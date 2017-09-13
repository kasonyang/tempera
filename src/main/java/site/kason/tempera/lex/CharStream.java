package site.kason.tempera.lex;
/**
 *
 * @author Kason Yang
 */
public interface CharStream {
    
    public final static int EOF = -1;
    
    public int consume();
    
    public int[] consume(int count);
    
    public void skip(int count);
    
    public int lookAhead(int count);
    
    public int getCurrentLine();
    
    public int getCurrentColumn();
    
    public int getCurrentOffset();

}
