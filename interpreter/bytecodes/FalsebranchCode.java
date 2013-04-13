/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs the virtual  machine to pop the top of the stack and 
 * check if its false.  If false, instructs the virtual machine to 
 * branch to label.  
 */
public class FalsebranchCode extends ByteCode {
    
    private String name = "FalsebranchCode";
    private String label;
    private int targetAddress;

    @Override
    public void init(Vector<String> args) {
        label = args.get(0);
        if (args.size() > 1) {
            targetAddress = Integer.parseInt(args.get(1));
        }
    }

    @Override
    public void execute(VirtualMachine vm) {
        //pop the top of the stack, if false (0) then branch to label
        if ( vm.popRunStack() == 0 ) {
            vm.setPC(targetAddress);
        } 
    }

    @Override
    public String getArgs() {
        return label;
    }

    @Override
    public String toString() {
        return "FALSEBRANCH " + label + "    " + targetAddress;
    }

}
