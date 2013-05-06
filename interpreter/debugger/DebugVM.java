package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import interpreter.bytecodes.ByteCode;
import java.util.ArrayList;
import java.util.List;
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
    ArrayList<Integer> breakTracker;
    ArrayList<Integer> lineCodeTracker;
    String debugCommand;
    Boolean stepOutIsPending;
    
    public DebugVM(Program aProgram, Vector<SourceLineEntry> source) {
        super(aProgram);
        pc = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = true;
        sourceByLine = source;
        environmentStack = new Stack<FunctionEnvironmentRecord>();
        breakTracker = new ArrayList<Integer>();
        //setup environmentStack with main
        FunctionEnvironmentRecord main = new FunctionEnvironmentRecord();
        main.setFunction("main", 1, sourceByLine.size()-1);
        main.setCurrentLine(1);
        environmentStack.add(main);
    }
    
    public void executeProgram() {

        int environmentStackSize = environmentStack.size();
        
        while (conditionCheck(environmentStackSize) && isRunning) {  //and checkBreak is false
            ByteCode code = program.getCode(pc);
            String n = code.getClass().getName();
            String[] fullName = n.split("\\.");
            String name;
            if (fullName.length == 4) {
                name = fullName[3]; 
            } else {
                name = fullName[2];
            }
            /*
            if (name.equals("LineCode")) {
                String lineno = code.getArgs();
                int temp = Integer.parseInt(lineno);
                Integer lineNumber = new Integer(temp);
                lineCodeTracker.add(lineNumber);
            }
            */
            
            //System.out.println("Name of code is: " + name);
            /*
            if (name.matches("ReadCode")) {
                System.out.println("Enter Integer: ");
            } 
            */
            
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
        debugCommand = null;
        System.out.println();
    }
    
    public void setToRun() {
        isRunning = true;
    }
    
    public void setCommand(String comm) {
        debugCommand = comm;
    }
    
    public Boolean okToSetBreak (String line) {//(int line) {//(String line) {
        
        Boolean set = false;
        /*
        Integer lineNumber = new Integer(line);
        if (lineCodeTracker.contains(lineNumber)) {
            set = true;
        }
        */
        if (line.contains("if") || line.contains("while") 
         || line.contains("int") || line.contains("boolean")
         || line.contains("=") || line.contains("{")
         || line.contains("return")){
            set = true;
        }
        return set;
    }
    
    public Boolean conditionCheck(int size) {
        Boolean check = false;
        if (debugCommand.equals("continue")) {
            check = (!isBreak(getCurrentLine())
                    || size == environmentStack.size());
        } else if (debugCommand.equals("out")) {
            check = environmentStack.size() >= size;
            if (isBreak(getCurrentLine()) && stepOutIsPending) {
                check = false;
            }
                //    && environmentStack.size() >= size); //< environmentStack.size());
        } else if (debugCommand.equals("in")) {
            //check = true;
        }
        
        return check; 
    }
    
    public void continueExecuting() {
        setCommand("continue");
        executeProgram();
    }
    
    public void stepOut() {
        setCommand("out");
        stepOutIsPending = true;
        executeProgram();
    }
    
    public void stepIn() {
        setCommand("in");
        executeProgram();
    }
    
    public String setBreak(int lineNum) {
        //check for proper conditions (LINE BC)
        Boolean breakSet = false;
        String successfulLines = "";
        if (lineNum <= sourceByLine.size()-1) {
            SourceLineEntry entry = sourceByLine.get(lineNum);
            String sourceLine = entry.getLine();
            if ( okToSetBreak(sourceLine)) { //(lineNum) ) {//(sourceLine)) {
                breakSet = entry.setBreak(Boolean.TRUE);
                sourceByLine.set(lineNum, entry);
                Integer lineno = new Integer(lineNum);
                if (breakSet) {
                    breakTracker.add(lineno);
                    successfulLines += lineno.toString() + " ";
                }
            } else {
                System.out.println("ERROR: Cannot set break at line: " + lineNum);
            }
            
        } else {
            System.out.println("ERROR: Line is out of range. Cannot set break at " + lineNum);
        }
        return successfulLines;
    }
    
    public Boolean clearBreak(int lineNum) {
        Boolean success = false;
        SourceLineEntry entry = sourceByLine.get(lineNum);
        entry.setBreak(Boolean.FALSE);
        sourceByLine.set(lineNum, entry);
        Integer line = new Integer(lineNum);
        breakTracker.remove(line);
        success = true;
        return success;
    }
    
    public String showBreaks() {
        String breakList = "";
        if (!breakTracker.isEmpty()) {
            for (int i = 0; i < breakTracker.size(); i++) {
                String number = breakTracker.get(i).toString();
                breakList += number;
                if (i < breakTracker.size()-1) {
                    breakList += ", ";
                }
            }
        } else {
            breakList += "No Breaks have been set at this time.";            
        }
        return breakList;
    }
    
    public Boolean isBreak(int lineNumber) {
        SourceLineEntry entry = sourceByLine.get(lineNumber);
        return entry.checkBreak();
    }
     
    public void setCurrentLine(int lineno) {
        //currentLine = lineno;
        if (lineno >= 1) {// && environmentStack.size() == runStack.framePointersNumber()) {
            FunctionEnvironmentRecord fer = environmentStack.pop();
            //int lastIndex = environmentStack.size() - 1;
            //FunctionEnvironmentRecord fer = environmentStack.get(lastIndex);
            fer.setCurrentLine(lineno);
            environmentStack.push(fer);
            //environmentStack.removeElementAt(lastIndex);
            
        }
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
        environmentStack.push(fer);
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
