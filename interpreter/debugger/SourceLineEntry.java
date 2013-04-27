package interpreter.debugger;

/**
 *
 * @author Rachel Sershon
 */
public class SourceLineEntry {
    
    Boolean isBreakSet;
    String sourceLine;
    
    public SourceLineEntry(String line) {
        isBreakSet = false;
        sourceLine = line;
    }
    
    void setBreak(Boolean breakFlag) {
        isBreakSet = breakFlag;
    }
    
    String getLine() {
        return sourceLine;
    }
    
    Boolean checkBreak(){
        return isBreakSet;
    }
    
}