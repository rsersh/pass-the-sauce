package interpreter.bytecodes.debuggerbytecodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;

/**
 *
 * @author Rachel Sershon
 */
public class LitCode extends interpreter.bytecodes.LitCode {
    
    public void execute(DebugVM vm) {
        super.execute(vm);
        int offsetLocation = vm.getStackSize()-1;
        vm.addPair(id, offsetLocation);
    }
    
    
}
