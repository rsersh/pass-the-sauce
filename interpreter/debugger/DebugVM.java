package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import java.util.Stack;
import java.util.Vector;

/**
 *
 * @author Rachel Sershon
 */
public class DebugVM extends interpreter.VirtualMachine {
    
    Program program;
    RunTimeStack runStack;
    int pc;
    Stack<Integer> returnAddrs;
    boolean isRunning;
    Vector<SourceLineEntry> sourceByLine;
    
    public DebugVM(Program aProgram, Vector<SourceLineEntry> source) {
        super(aProgram);
        sourceByLine = source;
    }
    
    public void setBreak(int lineNum) {
        
    }
    
    
}
