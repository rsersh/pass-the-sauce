/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Writes the value on the top of the stack to output, leaving the value
 * on top of the stack.
 */
public class WriteCode extends ByteCode {
    
    private String name = "WriteCode";
    private int targetAddress;

    @Override
    public void init(Vector<String> args) {
        if(args.size()>0) {
            targetAddress = Integer.parseInt(args.get(1));
        }
    }
    
    @Override
    public void execute(VirtualMachine vm) {
        //write the value on top of the stack to output
        //leave the value on top of the stack
        System.out.println("" + vm.peekRunStack());
    }

    @Override
    public String getArgs() {
        //provided to help resolve addresses
        return "Write";
    }

    @Override
    public String toString() {
        return "" + targetAddress;
    }

}