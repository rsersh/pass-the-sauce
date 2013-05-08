/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */

package interpreter.debugger;

import java.io.*;
import java.util.Vector;

/**
 * Loads the original source code into a vector of SourceLineEntries.
 * 
 */
public class SourceCodeLoader {
    
    BufferedReader source;
    static Vector<SourceLineEntry> sourceEntries;
    
    /** 
     * @param sourceFilename is a *.x file
     */
    public SourceCodeLoader(String sourceFile) throws IOException {
        sourceEntries = new Vector<SourceLineEntry>();
        try {
            source = new BufferedReader(new FileReader(sourceFile));
        } catch (IOException e) {
            System.out.println("ERROR: Problem opening file");
        }
    }
    
    /**
     *
     * @return a vector of SourceLineEntries 
     */
    public Vector<SourceLineEntry> loadEntries() {
        String line; 
        sourceEntries.add(new SourceLineEntry("DUMMY VALUE"));        
        try {
            while(source.ready()) {
                line = source.readLine();
                SourceLineEntry newEntry = new SourceLineEntry(line);
                sourceEntries.add(newEntry);
            }
        } catch(IOException e) {
            System.out.println("ERROR: Problem reading from .x source file.");
            System.exit(1);
        }
        
        return sourceEntries;
    }
    
    /**
     * 
     * @param lineNumber - line number of desired source code line
     * @return string of a line of code
     */
    public String getSourceLine(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        return entry.getLine();
    }
    
    /**
     *
     * @param lineNumber - from source code
     * @return true, if break point is set at given line number. false, if not.
     */
    public Boolean checkIsBreakSet(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        return entry.checkBreak();
    }
    
    /**
     * Sets a break point at the given line number.
     * @param lineNumber - from source code
     */
    public void setBreak(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        entry.setBreak(Boolean.TRUE);
        sourceEntries.set(lineNumber, entry);
    }
    
    /**
     * Clears the break point at the given line number.
     * @param lineNumber
     */
    public void resetBreak(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        entry.setBreak(Boolean.FALSE);
        sourceEntries.set(lineNumber, entry);
    }
    
    /**
     * Prints the source code by line number.
     */
    public void printSourceEntries() {
        int size = sourceEntries.size();
        for (int i = 1; i < size; i++) {
            System.out.println("" + i + ".  " + getSourceLine(i));
        }
    }

}
