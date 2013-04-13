/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Records and process the stack of active frames.  The RunTimeStack class 
 * maintains the stack of active frames; when we call a function we'll push
 * a new frame on the stack; when we return from a function we'll pop the 
 * top frame.
 */
public class RunTimeStack {
    
    ArrayList<Integer> runStack;  //need direct access for load and store
    Stack<Integer> framePointers; 
    int stackCounter;
    boolean dumpFlag;
    
    public RunTimeStack() {
        runStack = new ArrayList<Integer>();
        framePointers = new Stack<Integer>();
        stackCounter = 0;
        framePointers.add(0);        
        dumpFlag = false;
    }
    
    /**
     * Sets dumpFlag to ON or OFF
     * @param value  true or false
     */
    public void setDumpFlag(boolean value) {
        dumpFlag = value;
    }
    
    /**
     * Dumps the RunTimeStack information for debugging.
     */
    public void dump() {
        if (dumpFlag == true) {
            System.out.print("\n[");
            for (int i=0; i < runStack.size(); i++) {
                if (i!=0 && framePointers.contains(i)) {
                    System.out.print("] [");
                }
                if (!framePointers.contains(i)) {
                    System.out.print(", ");
                }
                System.out.print("" + runStack.get(i));
            }
            System.out.print("]");
        }
    }
    
    /**
     * Top entry is returned, but not removed.
     * @return the int at the top of the stack
     */
    public int peek() {
        int index = runStack.size() - 1;
        Integer top = runStack.get(index);  //does not remove
        return top.intValue(); 
    }

    /**
     * Top entry is removed and returned.
     * @return the int at the top of the stack 
     */
    public int pop() {
        int index = runStack.size() - 1;
        Integer top = runStack.remove(index);
        stackCounter--;
        return top.intValue();
    }
    
    /**
     * @param i  an int value to add to stack
     * @return the value just added
     */
    public int push(int i) {
        Integer newTop = new Integer(i);
        runStack.add(newTop);       
        stackCounter++;
        return newTop.intValue();
    }

    /**
     * Starts new activation record at indicated offset.
     * @param offset  the number of slots down from the top of the RunTimeStack
     */
    public void newFrameAt(int offset) {
        int programCounter = runStack.size();
        Integer off = new Integer(offset);
        framePointers.push(programCounter - off);
    }
    
    /**
     * Pop frame when returning from a function.  Before
     * popping, the return value is saved.  After popping,
     * the value is placed at the top of the stack.
     */
    public void popFrame()  {
        int pc = runStack.size();  //or could call getPC()
        //Integer returnValue = runStack.get(index);
        Integer returnValue = peek();
        Integer frameStart = (framePointers.pop()).intValue();
        for(int i = pc; i > frameStart; i--) {
            pop();
        }
        push(returnValue);
    }
    
    /**
     * @param offset location to frame start of where to store top of stack
     * @return the value just stored
     */
    public int store(int offset) {
        Integer newValue = peek();
        int frameStart = (framePointers.peek());
        int index = frameStart + offset;
        runStack.set(index, newValue);
        pop();
        return newValue.intValue();
    }
    
    /**
     * @param offset  location to frame start of value to load to top of stack
     * @return value just loaded
     */
    public int load(int offset) {
        int frameStart = (framePointers.peek()).intValue();
        int index = frameStart + offset;
        Integer newValue = runStack.get(index);
        push(newValue);
        return newValue.intValue();
    }
    
    /**
     * @param i  an Integer value to push to top of stack
     * @return value just pushed
     */
    public Integer push(Integer i) {
        runStack.add(i);
        stackCounter++;
        return i;
    }

    /**
     * @return current location of stack counter
     */
    public int getStackCounter() {
        return stackCounter;
    }
  
    /**
     * @return size of stack
     */
    public int getSize() {
        return runStack.size();
    }
}