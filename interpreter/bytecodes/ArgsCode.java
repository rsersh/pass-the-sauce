/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

/**
 * ARGS is used just prior to calling a function.  Instructs virtual machine
 * to set up a new frame the indicated numberOfValues down from the top of 
 * the stack.  This instruction is immediately followed by the CALL instruction.
 */
public class ArgsCode extends ByteCode {
    
    private String name = "ArgsCode";
    private int numberOfValues;
    
    @Override
    public void init(Vector<String> args) {
        String temp = args.get(0);
        numberOfValues = Integer.parseInt(temp);
    }

    @Override
    public void execute(VirtualMachine vm) {
        //set up a new frame numberOfValues down from the top
        vm.newActivationRecord(numberOfValues);
    }

    @Override
    public String getArgs() {
        String args = "";
        args += numberOfValues;
        return args;
    }   
    
    public String toString() {
        return "ARGS " + numberOfValues;
    }
}
