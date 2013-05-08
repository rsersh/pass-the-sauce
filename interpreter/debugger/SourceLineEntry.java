/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */

package interpreter.debugger;

/**
 *
 * Records a line of source code and whether a break point is set.
 */
public class SourceLineEntry {
    
    Boolean isBreakSet;
    String sourceLine;
    
    /**
     * Creates a new SourceLineEntry 
     * @param line - a line from source code
     */
    public SourceLineEntry(String line) {
        isBreakSet = false;
        sourceLine = line;
    }
    
    /**
     * Set a flag to true if a break point is set. False, if not.
     * @param breakFlag - true or false
     * @returns true
     */
    public Boolean setBreak(Boolean breakFlag) {
        isBreakSet = breakFlag;
        return true;
    }
    
    /**
     *
     * @return a line of source code
     */
    public String getLine() {
        return sourceLine;
    }
    
    /**
     *
     * @return true, if a break point has been set. false, if not.
     */
    public Boolean checkBreak(){
        return isBreakSet;
    }
    
}