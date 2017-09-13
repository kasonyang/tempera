package site.kason.tempera.lex;
/**
 *
 * @author Kason Yang
 */
public class StringCharStream implements CharStream {
    
    private int[] chars;
    
    private int offset = 0;
    
    private int currentLine = 1;
    
    private int lastChar;
    
    private static int COLUMN_START = 1;
    
    private int currentColumn = COLUMN_START;

    public StringCharStream(int[] chars) {
        this.chars = chars;
    }
    
    public StringCharStream(String str){
        int len = str.codePointCount(0,str.length());
        chars = new int[len];
        for(int i=0;i<len;i++){
            chars[i] = str.codePointAt(i);
        }
    }

    @Override
    public int consume() {
        if(this.offset>=this.chars.length) return EOF;
        int ch =  this.chars[this.offset++];
        boolean isNewLineChar = (ch == '\r') || (ch == '\n' && lastChar!='\r');
        if(isNewLineChar){
            this.currentLine++;
            this.currentColumn = COLUMN_START;
        }else{
            this.currentColumn++;
        }
        lastChar = ch;
        return ch;
    }

    @Override
    public int[] consume(int count) {
        int remaining = this.chars.length - this.offset;
        if(remaining<count){
            throw new RuntimeException("chars not enough," + remaining + " remaining.");
        }
        int[] res = new int[count];
        for(int i=0;i<count;i++){
            res[i] = this.consume();
        }
        return res;
    }

    @Override
    public int lookAhead(int count) {
        int lookOffset = this.offset + count - 1;
        if(lookOffset>=this.chars.length){
            return EOF;
        }
        return this.chars[lookOffset];
    }

    @Override
    public void skip(int count) {
        for(int i=0;i<count;i++){
            this.consume();
        }
    }

    @Override
    public int getCurrentLine() {
        return this.currentLine;
    }

    @Override
    public int getCurrentColumn() {
        return this.currentColumn;
    }

    /**
     * the current caret of stream,zero-based.
     * @return 
     */
    @Override
    public int getCurrentOffset() {
        return offset;
    }

}
