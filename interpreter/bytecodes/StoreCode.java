/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs virtual machine to store value at top of stack in location:
 * offset + frameStart
 */
public class StoreCode extends ByteCode {
    
    private String name = "StoreCode";
    private int offset;
    private String label;

    @Override
    public void init(Vector<String> args) {
        if (args.size() < 2) {
            System.out.println("Insufficient args for StoreCode");
            System.exit(1);
        }
        offset = Integer.parseInt(args.get(0));
        label = args.get(1);
    }
        
    @Override
    public void execute(VirtualMachine vm) {
        //pop the top of the stack and store value into 
        //the offset n from the start of the frame
        vm.store(offset);
    }

    @Override
    public String getArgs() {
        String args = "";
        args += "offset = " + offset + "  label: " + label;
        return args;
    }

    @Override
    public String toString() {
        //hasn't executed yet
        return "STORE " + offset + " " + label + "    " 
                + label + " = "; //need to add  + vm.getValueAtOffset(offset); 
    }
    
}
