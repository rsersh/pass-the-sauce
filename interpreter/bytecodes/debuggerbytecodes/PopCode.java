/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */
package interpreter.bytecodes.debuggerbytecodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;

/**
 * Instructs virtual machine to pop the top indicated number of levels 
 * of runtime stack and the indicated number of FunctionEnvironmentRecords.
 */
public class PopCode extends interpreter.bytecodes.PopCode {
    
    public void execute(DebugVM vm) {
        super.execute(vm);
        vm.popPairs(levels);
    }
}
