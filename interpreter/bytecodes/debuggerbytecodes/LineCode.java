/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */
package interpreter.bytecodes.debuggerbytecodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;
import java.util.Vector;

/**
 * Instructs virtual machine to set the currentLine in the FER to 
 * the currentSourceLine.
 */
public class LineCode extends interpreter.bytecodes.ByteCode {

    //the generated bytecodes for currentSourceLine follow this bytecode
    int currentSourceLine;  
    
    @Override
    public String getArgs() {
        String args = "";
        args += currentSourceLine;
        return args;
    }

    @Override
    public void init(Vector<String> args) {
        String temp = args.get(0);
        currentSourceLine = Integer.parseInt(temp);
    }

    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM)vm);
    }
    
    public void execute(DebugVM vm) {
        vm.setCurrentLine(currentSourceLine);
    }

    @Override
    public String toString() {
        return "LINE " + currentSourceLine;
    }
    
}
