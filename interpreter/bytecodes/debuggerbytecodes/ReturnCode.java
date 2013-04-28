package interpreter.bytecodes.debuggerbytecodes;

import interpreter.debugger.DebugVM;

/**
 *
 * @author Rachel Sershon
 */
public class ReturnCode extends interpreter.bytecodes.ReturnCode {
    //pop FER
    
    public void execute(DebugVM vm) {
        super.execute(vm);
        vm.popEnvironmentEntry();
    }
    
}
