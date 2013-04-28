package interpreter.bytecodes.debuggerbytecodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;
//import interpreter.bytecodes.PopCode;

/**
 *
 * @author Rachel Sershon
 */
public class PopCode extends interpreter.bytecodes.PopCode {
    
    //need to pop FER
    
    public void execute(DebugVM vm) {
        super.execute(vm);
        vm.popPairs(levels);
    }
}
