/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */

package interpreter.debugger;

import interpreter.Program;
import interpreter.RunTimeStack;
import interpreter.bytecodes.ByteCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

/**
 * Virtual Machine for use in Debug mode.
 * 
 */
public class DebugVM extends interpreter.VirtualMachine {
    

    Stack<FunctionEnvironmentRecord> environmentStack;
    Vector<SourceLineEntry> sourceByLine;
    ArrayList<Integer> breakTracker;
    ArrayList<Integer> lineCodeTracker;
    String debugCommand;
    Boolean stepIsPending;
    Boolean isTraceOn;
    String codeName;
    int originalStartingLine;
    
    
    
    /**
     * Creates a virtual machine that can be used for debugging.
     * @param aProgram - loaded program with resolved addresses
     * @param source - original source code stored by line number
     */
    public DebugVM(Program aProgram, Vector<SourceLineEntry> source) {
        super(aProgram);
        pc = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = true;
        stepIsPending = false;
        sourceByLine = source;
        environmentStack = new Stack<FunctionEnvironmentRecord>();
        breakTracker = new ArrayList<Integer>();
        //setup environmentStack with main
        FunctionEnvironmentRecord main = new FunctionEnvironmentRecord();
        main.setFunctionName("main");
        main.setStartLine(1);
        main.setEndLine(sourceByLine.size()-1);
        //main.setFunction("main", 1, sourceByLine.size()-1);
        main.setCurrentLine(1);
        environmentStack.add(main);
    }
    
    
    
    @Override
    public void executeProgram() {

        int environmentStackSize = environmentStack.size();
        
        while (conditionCheck(environmentStackSize) && isRunning) {  //and checkBreak is false
            ByteCode code = program.getCode(pc);
            String n = code.getClass().getName();
            String[] fullName = n.split("\\.");
            //String name;
            if (fullName.length == 4) {
                codeName = fullName[3]; 
            } else {
                codeName = fullName[2];
            }
            /*
            if (name.equals("LineCode")) {
                String lineno = code.getArgs();
                int temp = Integer.parseInt(lineno);
                Integer lineNumber = new Integer(temp);
                lineCodeTracker.add(lineNumber);
            }
            */           
            code.execute(this);
            
            if (codeName.equals("DumpCode")) {
                System.out.print("\n" + code.toString());
                //need to peek at top of stack for output for StoreCode & ReturnCode
                if (codeName.equals("StoreCode") || codeName.equals("ReturnCode")) {
                    System.out.print("" + peekRunStack());
                }
                if (codeName.equals("CallCode"))  {
                    System.out.print("(" + peekRunStack() + ")");
                }
            
            }
            runStack.dump(); 
            
            pc++;
        }  
        debugCommand = null;
        System.out.println();
    }
    
    /**
     * Set the Virtual Machine to run.
     */
    public void setToRun() {
        isRunning = true;
    }
    
    /**
     * Sets the debugCommand.
     * @param comm - the debug command requested by user
     */
    public void setCommand(String comm) {
        debugCommand = comm;
    }
    
    /**
     * Checks to see if the given line can have a breat set.
     * @param line
     * @return true if break can be set on requested line; false, if not.
     */
    public Boolean okToSetBreak (String line) {
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
    
    /**
     * Based on the debugCommand, allows VM to execute only under correct
     * conditions. 
     * @param size - the size of the environmentStack before execution
     * @return true or false, depending on whether the conditions are right
     *     to execute given the debugCommand.
     */
    public Boolean conditionCheck(int size) {
        Boolean check = false;
        if (debugCommand.equals("continue")) {
            check = (!isBreak(getCurrentLine())
                    || size == environmentStack.size());
        } else if (debugCommand.equals("out")) {
            check = environmentStack.size() >= size || !isBreak(getCurrentLine());
            //System.out.println("*****Check is " + check);
            //System.out.println("current size since out was set:" + environmentStack.size() + " / original size: " + size);
            //want to execute until function endLine + 1
            FunctionEnvironmentRecord fer = environmentStack.peek();
            int end = fer.getEndLine() + 1;
            if (end == environmentStack.peek().getCurrentLine()) {
                check = false;
            }
            
        } else if (debugCommand.equals("in")) {
            //once a fer is added, stop
            check = environmentStack.size() <= size;
            //add formals if function code is hit
            if (!check && codeName.equals("FunctionCode") 
                    && environmentStack.peek().getStartLine() > 0) {
                check = true;
            }

        } else if (debugCommand.equals("over")) {
            if (environmentStack.peek().getCurrentLine() != originalStartingLine + 1) {
                check = true;
            } else {
                check = false;
            }

        } else if (debugCommand.equals("trace")) {
            if (isTraceOn) {
                
            }
            
        }
        
        return check; 
    }
    
    /**
     * Sets debug command and executesProgram.
     */
    public void continueExecuting() {
        setCommand("continue");
        executeProgram();
    }
    
   /**
     * Sets debug command and executesProgram.
     */
    public void stepOut() {
        setCommand("out");
        executeProgram();
    }
  
   /**
     * Sets debug command, sets stepIsPending, and executesProgram.
     */
    public void stepIn() {
        setCommand("in");
        stepIsPending = true;
        executeProgram();
    }
    
   /**
     * Sets debug command, sets originalStartingLine, and executesProgram.
     */
    public void stepOver() {
        setCommand("over");
        FunctionEnvironmentRecord fer = environmentStack.peek();
        originalStartingLine = fer.getCurrentLine();
        executeProgram();
        //set condition to stop (after just one line)?
    }
    
   /**
     * Sets debug command, trace flag, and executesProgram.
     */
    public void trace() {
        setCommand("trace");
        isTraceOn = true;
        
    }
    
    public void printCallStack() {
        
    }
    
    
    /**
     * Sets breakpoints at given line number.
     * @param lineNum - a line number in the source code
     * @return returns a string of line numbers that have breakpoints set
     */
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
    
    /**
     * Clears the breakpoints at given line number.
     * @param lineNum - a line number in the source code
     * @return true if breakpoint was successfully cleared 
     */
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
    
    /**
     * 
     * @return string of line numbers that have breakpoints set
     */
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
    
    /**
     * Checks to see if a given line number has a breakpoint set.
     * @param lineNumber - line number to check for breakpoint
     * @return true, if a breakpoint is set at the given line number.
     */
    public Boolean isBreak(int lineNumber) {
        SourceLineEntry entry = sourceByLine.get(lineNumber);
        return entry.checkBreak();
    }
     
    /**
     * Sets the currentLine in the FunctionEnvironmentRecord.
     * @param lineno - line number
     */
    public void setCurrentLine(int lineno) {
        if (lineno >= 1) {
            FunctionEnvironmentRecord fer = environmentStack.pop();
            fer.setCurrentLine(lineno);
            environmentStack.push(fer);
        }
    }
    
    /**
     *
     * @return the current line number
     */
    public int getCurrentLine() {
        FunctionEnvironmentRecord fer = environmentStack.peek();
        return fer.getCurrentLine();
    }
    
    /**
     * Stops the Virtual Machine from executing bytecodes.
     */
    public void quit() {
        stopRunning();
    }
    
    /**
     * For use by the FunctionCode ByteCode to set the FunctionEnviromentRecord.
     * @param name  - name of function
     * @param startLine  - start line of function
     * @param endLine  - end line of function
     */
    public void addFER(String name, int startLine, int endLine) {
        FunctionEnvironmentRecord fer = new FunctionEnvironmentRecord();
        fer.setFunctionName(name);
        fer.setStartLine(startLine);
        fer.setEndLine(endLine);
        //fer.setFunction(name, startLine, endLine);
        environmentStack.push(fer);
    }
    
    /**
     * Adds an id/offset pair to the current FunctionEnvironmentRecord.
     * @param id  - id of value
     * @param offset - offset of id on stack
     */
    public void addPair(String id, int offset) {
        int location = offset + (super.getStackSize()-1);
        FunctionEnvironmentRecord fer = environmentStack.pop();
        fer.enterPair(id, location);
        environmentStack.push(fer);
    }
    
    /**
     * Removes the given number of id/offset pairs from the FunctionEnvironmentRecord.
     * @param numberOfPops - the number of pairs to remove
     */
    public void popPairs(int numberOfPops) {
        FunctionEnvironmentRecord fer = environmentStack.pop();
        fer.popPairs(numberOfPops);
        environmentStack.push(fer);
    }    
    
    /**
     *  Removes the last FunctionEnvironmentRecord from the environmentStack.
     */
    public void popEnvironmentEntry() {
        environmentStack.pop();
    }
    
    /**
     * Displays the current function. * indicates a breakpoint has been set
     * and <----- indicates the line awaiting execution.
     * @return
     */
    public String displayFunc(){
        FunctionEnvironmentRecord fer = environmentStack.peek();
        int start = fer.getStartLine();
        int end = fer.getEndLine();
        int current = fer.getCurrentLine();
        String name = fer.getName();
        String functionOutput = ""; 
        
        /*
        if (stepIsPending == true) {
            current = fer.getCurrentLine()+1;
        } 
        */
        
        if (start < 0) {
            functionOutput += "******" + name + "******";
        } else {
            for(int i = start; i <= end; i++) {
                Integer temp = new Integer(i);
                if (breakTracker.contains(temp)) {
                    functionOutput += "*";
                }
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
    
    
    /**
     * 
     * @return a String of id/offset pairs in the current function
     */
    public String displayVars() {
        FunctionEnvironmentRecord fer = environmentStack.peek();
        return fer.symtab.printBindings();
    }
}