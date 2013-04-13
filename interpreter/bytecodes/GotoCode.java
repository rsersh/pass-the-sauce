/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs virtual machine to set program counter to targetAddress of label.
 */
public class GotoCode extends ByteCode {
    
    private String name = "GotoCode";
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
        vm.setPC(targetAddress);
    }

    @Override
    public String getArgs() {
        return label;
    }

    @Override
    public String toString() {
        return "GOTO " + label + "    " + targetAddress;
    }
    
}
