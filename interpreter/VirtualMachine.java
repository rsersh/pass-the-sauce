/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter;

import interpreter.bytecodes.ByteCode;
import java.util.Stack;

/**
 * The VirtualMachine executes the bytecodes. 
 */
public class VirtualMachine {
    
    RunTimeStack runStack;
    int pc;
    Stack<Integer> returnAddrs;
    boolean isRunning;
    Program program;

    /**
     * @param program  Program object loaded with bytecodes containing all information
     *                 needed for execution
     */
    public VirtualMachine(Program program) {
        this.program = program;
    }
    
    /**
     * Signal to stop running the fetch/execute cycle.
     */
    public void stopRunning() {
        isRunning = false;
    }
    
    /**
     * @param returnLocation to return after function executes
     */
    public void addReturn(int returnLocation) {
        returnAddrs.push(new Integer(returnLocation));
    }
    
    /**
     * @return location to go next for execution
     */
    public int getReturn() {
        Integer returnValue = returnAddrs.pop();
        return returnValue.intValue();
    }

    /**
     * @param i  an Integer value to push to stack
     */
    public void pushRunStack(Integer i) {
        runStack.push(i);
        //pc++;
    }
    
    /**
     * @param i an int value to push to stack
     */
    public void pushRunStack(int i) {
        runStack.push(i);
        //pc++;
    }
    
    /**
     * @return value at top of stack (value is not removed)
     */
    public int peekRunStack() {
        return runStack.peek();
    }
    
    /**
     * @return value at top of stack (value is removed from stack)
     */
    public int popRunStack() {
        int value = runStack.pop();
        //pc--;
        return value;
    }
    
    /**
     * @param offset  location (from top of stack) to begin a new frame
     */
    public void newActivationRecord(int offset) {
        runStack.newFrameAt(offset);
    }
    
    public void popActivationRecord() {
        runStack.popFrame();
    }
    
    /**
     * @param offset location from frame start to store top of stack
     */
    public void store(int offset) {
        runStack.store(offset);
    }
    
    /**
     * @param offset   location from frame start of value to push to top of stack
     */
    public void load(int offset) {
        runStack.load(offset);
    }
    
    /**
     * @return size of stack
     */
    public int getStackSize() {
        return runStack.getSize();
    }
    
    /**
     * @param newLocation for program counter to be set to
     */
    public void setPC(int newLocation) {
        pc = newLocation;
    }
    
    /**
     * @return the current location in stack
     */
    public int getPC() {
        return pc;
    }
    
    /**
     * @param flag  boolean to set dumping on or off
     */
    public void setDump(boolean flag) {
        runStack.setDumpFlag(flag);
    }
    
    /**
     * @return current location on the stack
     */
    public int getRunStackCounter() {
        return runStack.getStackCounter();
    }
    
    /**
     * Given by Prof. Levine, this function uses a Fetch/Execute Cycle
     * Design Pattern.  Dynamic binding is used to execute each bytecode
     * as it is read in from the program.
     */
    public void executeProgram() {
        pc = 0;
        runStack = new RunTimeStack();
        returnAddrs = new Stack<Integer>();
        isRunning = true;
        while (isRunning) {
            ByteCode code = program.getCode(pc);
            String n = code.getClass().getName();
            String[] fullName = n.split("\\.");
            String name = fullName[2];
            
            code.execute(this);

            System.out.print("\n" + code.toString());
            //need to peek at top of stack for output for StoreCode & ReturnCode
            if (name.equals("StoreCode") || name.equals("ReturnCode")) {
                System.out.print("" + peekRunStack());
            }
            runStack.dump(); 
            pc++;
        }
        System.out.println();
    }
    
}
