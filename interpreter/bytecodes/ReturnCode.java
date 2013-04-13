/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs virtual machine to pop the current frame and return to callee.
 */
public class ReturnCode extends ByteCode {
    
    private String name = "ReturnCode";
    private String label;

    @Override
    public void init(Vector<String> args) {
        if(args.size() > 0) {
            label = args.get(0);
        }
    } 

    @Override
    public void execute(VirtualMachine vm) {
        //popframe
        vm.popActivationRecord();
        //reset pc
        int returnLocation = vm.getReturn();
        vm.setPC(returnLocation);
    }

    @Override
    public String getArgs() {
        return label;
    } 

    @Override
    public String toString() {
        String returnString = "RETURN ";
        if (label == null) {
            return returnString;
        } else {
            String[] parsedLabel = label.split("<");
            String strippedLabel = parsedLabel[0];
            returnString += label + "  exit " + strippedLabel + ": ";
        } 
        return returnString;
    }
}
