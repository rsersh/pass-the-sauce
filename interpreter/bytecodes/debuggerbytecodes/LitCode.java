/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */
package interpreter.bytecodes.debuggerbytecodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;

/**
 * Instructs virtual machine to push the given value to the top of the runstack
 * and to add the id and offset to the symbol table/FER, as a pair.
 */
public class LitCode extends interpreter.bytecodes.LitCode {
    
    public void execute(DebugVM vm) {
        super.execute(vm);
        int offsetLocation = vm.getStackSize()-1;
        vm.addPair(id, offsetLocation);
    }
    
    
}
