/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs virtual machine to stop execution.
 */
public class HaltCode extends ByteCode {
    
    private String name = "HaltCode";
    
    @Override
    public void init(Vector<String> args) {
        
    }
    
    @Override
    public void execute(VirtualMachine vm) {
        //halt execution
        vm.stopRunning();
    }

    @Override
    public String getArgs() {
        return "NoArgs";
    }

    @Override
    public String toString() {
        return "HALT ";
    }
    
}
