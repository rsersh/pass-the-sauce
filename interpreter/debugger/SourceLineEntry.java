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
    
    Boolean setBreak(Boolean breakFlag) {
        isBreakSet = breakFlag;
        return true;
    }
    
    String getLine() {
        return sourceLine;
    }
    
    Boolean checkBreak(){
        return isBreakSet;
    }
    
}