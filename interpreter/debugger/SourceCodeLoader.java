package interpreter.debugger;

import java.io.*;
import java.util.Vector;

/**
 *
 * @author Rachel Sershon
 */
public class SourceCodeLoader {
    
    //load() method returns a vector of sourcelineentries
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
    
    public String getSourceLine(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        return entry.getLine();
    }
    
    public Boolean checkIsBreakSet(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        return entry.checkBreak();
    }
    
    public void setBreak(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        entry.setBreak(Boolean.TRUE);
        sourceEntries.set(lineNumber, entry);
    }
    
    public void resetBreak(int lineNumber) {
        SourceLineEntry entry = sourceEntries.get(lineNumber);
        entry.setBreak(Boolean.FALSE);
        sourceEntries.set(lineNumber, entry);
    }
    
    public void printSourceEntries() {
        int size = sourceEntries.size();
        for (int i = 1; i < size; i++) {
            System.out.println("" + i + ".  " + getSourceLine(i));
        }
    }

    public static void main(String[] args) throws IOException {
        SourceCodeLoader testSCL = new SourceCodeLoader("test.x.cod");
        Vector<SourceLineEntry> testEntries = testSCL.loadEntries();
        //you've loaded the entries so can now access through sourceEntries
        System.out.println("Line 2: " + testSCL.getSourceLine(2));
        
        SourceLineEntry entry = testEntries.get(4);
        System.out.println("Line 4: " + testSCL.getSourceLine(4));
        System.out.print("\nBreak at Line 4: ");
        if (entry.checkBreak()) {
           System.out.print(" TRUE\n");
        } else {
           System.out.print(" FALSE\n");
        }
        
        Boolean flag = testSCL.checkIsBreakSet(6);
        if (flag) {
            System.out.println("Break at Line 6: TRUE");
        } else {
            System.out.println("Break at Line 6: FALSE");
        }
        
        //Boolean newFlag = true;
        testSCL.setBreak(6);
        Boolean f = testSCL.checkIsBreakSet(6);
        if (f) {
            System.out.println("Break at Line 6: TRUE");
        } else {
            System.out.println("Break at Line 6: FALSE");
        }
        testSCL.resetBreak(6);
        Boolean r = testSCL.checkIsBreakSet(6);
        if (r) {
            System.out.println("Break at Line 6: TRUE");
        } else {
            System.out.println("Break at Line 6: FALSE");
        }
        
        System.out.println("********************");
        //testSCL.printSourceEntries();

    }
}
