package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import interpreter.bytecodes.ByteCode;
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
    Stack<Integer> breakTracker;
    String command;
    
    public DebugVM(Program aProgram, Vector<SourceLineEntry> source) {
        super(aProgram);
        sourceByLine = source;
        environmentStack = new Stack<FunctionEnvironmentRecord>();
        breakTracker = new Stack<Integer>();
        //setup environmentStack with main
        FunctionEnvironmentRecord main = new FunctionEnvironmentRecord();
        main.setFunction("main", 1, sourceByLine.size()-1);
        main.setCurrentLine(1);
        environmentStack.add(main);
    }
    
    public void executeProgram() {
        pc = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = true;
        int environmentStackSize = environmentStack.size();
        
        while (conditionCheck(environmentStackSize) && isRunning) {  //and checkBreak is false
            ByteCode code = program.getCode(pc);
            String n = code.getClass().getName();
            String[] fullName = n.split("\\.");
            String name = fullName[2];
            
            code.execute(this);
            if (name.equals("DumpCode")) {
                System.out.print("\n" + code.toString());
                //need to peek at top of stack for output for StoreCode & ReturnCode
                if (name.equals("StoreCode") || name.equals("ReturnCode")) {
                    System.out.print("" + peekRunStack());
                }
                if (name.equals("CallCode"))  {
                    System.out.print("(" + peekRunStack() + ")");
                }
            }
            runStack.dump(); 
            pc++;
        }  //where to reset command?
        System.out.println();
    }
    
    public void setToRun() {
        isRunning = true;
    }
    
    public void setCommand(String comm) {
        command = comm;
    }
    
    public Boolean okToSetBreak(String line) {
        Boolean set = false;
        if (line.contains("if") || line.contains("while") 
         || line.contains("int") || line.contains("boolean")
         || line.contains("=") || line.contains("{")
         || line.contains("return")){
            set = true;
        }
        return set;
    }
    
    public Boolean conditionCheck(int size) {
        if (command.equals("continue")) {
            return (!isBreak(getCurrentLine()) 
                    || size == environmentStack.size());
        } else if (command.equals("out")) {
            return (!isBreak(getCurrentLine())
                    || size >= environmentStack.size());
        } else if (command.equals("in")) {
            return true;
        }
        
        else { return true;  }
    }
    
    public void continueExecuting() {
        setCommand("continue");
    }
    
    public void stepOut() {
        setCommand("out");
    }
    
    public void stepIn() {
        setCommand("in");
    }
    
    public void setBreak(int lineNum) {
        //check for proper conditions (LINE BC)
        if (lineNum <= sourceByLine.size()-1) {
            SourceLineEntry entry = sourceByLine.get(lineNum);
            String sourceLine = entry.getLine();
            if (okToSetBreak(sourceLine)) {
                entry.setBreak(Boolean.TRUE);
                sourceByLine.set(lineNum, entry);
                Integer lineno = new Integer(lineNum);
                breakTracker.push(lineNum);
            } else {
                System.out.println("ERROR: Cannot set break at line: " + lineNum);
            }
        } else {
            System.out.println("ERROR: Line is out of range. Cannot set break.");
        }
    }
    
    public void clearBreak(int lineNum) {
        SourceLineEntry entry = sourceByLine.get(lineNum);
        entry.setBreak(Boolean.FALSE);
        sourceByLine.set(lineNum, entry);
        Integer line = new Integer(lineNum);
        breakTracker.remove(line);
    }
    
    public String listBreaks() {
        String breakList = "";
        for (int i = 0; i < breakTracker.size(); i++) {
            String number = (breakTracker.pop()).toString();
            breakList += number;
            if (i < breakTracker.size()) {
                breakList += ", ";
            }
        }
        return breakList;
    }
    
    public Boolean isBreak(int lineNumber) {
        SourceLineEntry entry = sourceByLine.get(lineNumber);
        return entry.checkBreak();
    }
     
    public void setCurrentLine(int lineno) {
        //currentLine = lineno;
        FunctionEnvironmentRecord fer = environmentStack.pop();
        fer.setCurrentLine(lineno);
        environmentStack.push(fer);
    }
    
    public int getCurrentLine() {
        FunctionEnvironmentRecord fer = environmentStack.peek();
        return fer.getCurrentLine();
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
            for(int i = start; i <= end; i++) {
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
