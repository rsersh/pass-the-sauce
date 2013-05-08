/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */
package interpreter.bytecodes.debuggerbytecodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;
import interpreter.debugger.FunctionEnvironmentRecord;
import java.util.Vector;

/**
 * Instructs virtual machine to set the name, startLine, and endLine of
 * the current function in the current FunctionEnvironmentRecord.
 */
public class FunctionCode extends interpreter.bytecodes.ByteCode  {

    String name;
    int startLine;
    int endLine;
    
    @Override
    public String getArgs() {
        return name + ", " + startLine + ", "+ endLine;
    }

    @Override
    public void init(Vector<String> args) {
        name = args.get(0);
        startLine = Integer.parseInt(args.get(1));
        endLine = Integer.parseInt(args.get(2));
    }

    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }
    
    public void execute(DebugVM vm) {
        //start a new fer with name, startLine, and endLine
        vm.addFER(name, startLine, endLine);
        vm.setCurrentLine(startLine);
    }

    @Override
    public String toString() {
        return name + ", " + startLine + ", "+ endLine;
    }
    
}
