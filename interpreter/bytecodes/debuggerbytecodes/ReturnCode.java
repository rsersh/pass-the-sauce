/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */
package interpreter.bytecodes.debuggerbytecodes;

import interpreter.debugger.DebugVM;

/**
 * Instructs virtual machine to pop the current frame, pop the  
 * last FunctionEnvironmentRecord, and return to callee.
 */
public class ReturnCode extends interpreter.bytecodes.ReturnCode {
    
    
    public void execute(DebugVM vm) {
        super.execute(vm);
        vm.popEnvironmentEntry();
    }
    
}
