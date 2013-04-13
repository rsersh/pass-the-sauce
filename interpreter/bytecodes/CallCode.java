/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs virtual machine to transfers control to the indicated function.
 * This instruction should be preceeded by the ARGS instruction.
 */
public class CallCode extends ByteCode {

    private String name = "CallCode";
    private String funcName;
    private int targetAddress;
    
    @Override
    public void init(Vector<String> args) {
        funcName = args.get(0);
        if (args.size() > 1) {
            targetAddress = Integer.parseInt(args.get(1));
        }
        //funcName = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        //tranfer control to the indicated function
        int returnLocation = vm.getPC();
        vm.addReturn(returnLocation);
        vm.setPC(targetAddress);
    }

    @Override
    public String getArgs() {
        return funcName;
    }

    @Override
    public String toString() {
        String stripped[] = funcName.split("<<");
        return "CALL " + funcName + "    " 
                + stripped[0] + "(" + targetAddress + ")";
    }
      
}
