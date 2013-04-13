/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs virtual machine to pop the top indicated number of levels 
 * of runtime stack.
 */
public class PopCode extends ByteCode {

    private String name = "PopCode";
    private int levels;
    
    @Override
    public void init(Vector<String> args) {
        String temp = args.get(0);
        levels = Integer.parseInt(temp);
    }
    
    @Override
    public void execute(VirtualMachine vm) {
        //pop top n levels of runtime stack
        for (int i = 0; i < levels; i++) {
            vm.popRunStack();
        }
        
    }

    @Override
    public String getArgs() {
        String args = "";
        args += levels;
        return args;
    }

    @Override
    public String toString() {
        return "POP " + levels;
    }
}