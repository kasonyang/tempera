package site.kason.tempera.lex;

/**
 *
 * @author Kason Yang
 */
public class OffsetRange {

    private int startOffset,
            stopOffset,
            startLine,
            startLineColumn,
            stopLine,
            stopLineColumn;

    public final static OffsetRange NONE = new OffsetRange();

    public OffsetRange(int startOffset, int stopOffset, int startLine, int startLineColumn, int stopLine, int stopLineColumn) {
        this.startOffset = startOffset;
        this.stopOffset = stopOffset;
        this.startLine = startLine;
        this.startLineColumn = startLineColumn;
        this.stopLine = stopLine;
        this.stopLineColumn = stopLineColumn;
    }

    public OffsetRange() {
        startOffset = -1;
        stopOffset = -1;
        startLine = -1;
        startLineColumn = -1;
        stopLine = -1;
        stopLineColumn = -1;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", startLine, startLineColumn);
    }

    public int getStartOffset() {
        return startOffset;
    }

    public int getStopOffset() {
        return stopOffset;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getStartLineColumn() {
        return startLineColumn;
    }

    public int getStopLine() {
        return stopLine;
    }

    public int getStopLineColumn() {
        return stopLineColumn;
    }
    
    

}
