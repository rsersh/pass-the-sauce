package interpreter.bytecodes.debuggerbytecodes;

import interpreter.VirtualMachine;
import interpreter.debugger.DebugVM;
import java.util.Vector;

/**
 *
 * @author Rachel Sershon
 */
public class FormalCode extends interpreter.bytecodes.ByteCode {

    String id;
    int offset;
    
    @Override
    public String getArgs() {
        return id + " " + offset;
    }

    @Override
    public void init(Vector<String> args) {
        id = args.get(0);
        offset = Integer.parseInt(args.get(1));
    }

    @Override
    public void execute(VirtualMachine vm) {
        execute((DebugVM) vm);
    }
    
    public void execute(DebugVM vm) {
        vm.addPair(id, offset);
    }

    @Override
    public String toString() {
        return "FORMAL " + id + " " + offset;
    }
    
}
