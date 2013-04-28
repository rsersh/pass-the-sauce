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
    
    //int currentLine;
    Stack<FunctionEnvironmentRecord> environmentStack;
    Vector<SourceLineEntry> sourceByLine;
    
    public DebugVM(Program aProgram, Vector<SourceLineEntry> source) {
        super(aProgram);
        environmentStack = new Stack<FunctionEnvironmentRecord>();
        sourceByLine = source;
        
        //setup environmentStack with main
        FunctionEnvironmentRecord main = new FunctionEnvironmentRecord();
        main.setFunction("main", 1, sourceByLine.size()-1);
        main.setCurrentLine(1);
        environmentStack.add(main);
    }
    
    public Boolean isRunning() {
        return isRunning;
    }
    
    public void setBreak(int lineNum) {
        //check for proper conditions (LINE BC)
        SourceLineEntry entry = sourceByLine.get(lineNum);
        entry.setBreak(Boolean.TRUE);
        sourceByLine.set(lineNum, entry);
    }
    
    public void clearBreak(int lineNum) {
        SourceLineEntry entry = sourceByLine.get(lineNum);
        entry.setBreak(Boolean.FALSE);
        sourceByLine.set(lineNum, entry);
    }
    
    /*
    public void continueExecution() {
        //isRunning = true;
        super.executeProgram();
    }
    */
    
    public void setCurrentLine(int lineno) {
        //currentLine = lineno;
        FunctionEnvironmentRecord fer = environmentStack.pop();
        fer.setCurrentLine(lineno);
        environmentStack.push(fer);
    }
    
    public void quit() {
        stopRunning();
    }
    
    public void addFER(String name, int startLine, int endLine) {
        FunctionEnvironmentRecord fer = new FunctionEnvironmentRecord();
        fer.setFunction(name, startLine, endLine);
        environmentStack.add(fer);
    }
    
    public void addPair(String id, int offset) {
        int location = offset + (super.getStackSize()-1);
        FunctionEnvironmentRecord fer = environmentStack.pop();
        fer.enterPair(id, location);
        environmentStack.push(fer);
    }
    
    public void popPairs(int numberOfPops) {
        FunctionEnvironmentRecord fer = environmentStack.pop();
        fer.popPairs(numberOfPops);
        environmentStack.push(fer);
    }    
    
    public void popEnvironmentEntry() {
        environmentStack.pop();
    }
    
    public String displayFunc(){
        FunctionEnvironmentRecord fer = environmentStack.peek();
        int start = fer.getStartLine();
        int end = fer.getEndLine();
        int current = fer.getCurrentLine();
        String name = fer.getName();
        String functionOutput = ""; 
        
        if (start < 0) {
            functionOutput += "******" + name + "******";
        } else {
            for(int i = start; i < end; i++) {
                functionOutput += "" + i + ".  " 
                        + (sourceByLine.get(i)).getLine();
                if (i == current) {
                    functionOutput += "  <-----";
                }
                functionOutput += "\n";
            }
        }
        
        return functionOutput;
    }
    
    public String displayVars() {
        FunctionEnvironmentRecord fer = environmentStack.peek();
        return fer.symtab.printBindings();
       // return "displayVars() not implemented yet";
    }
}
