/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

/**
 * Instructs the virtual machine to set a flag ON or OFF in order 
 * to determine if stack contents should be printed to output.
 */
public class DumpCode extends ByteCode {

    private String name = "DumpCode";
    private String dumpFlag;

    @Override
    public String getArgs() {
        return dumpFlag;
    }

    @Override
    public void init(Vector<String> args) {
        dumpFlag = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        Boolean value = false;
        if (dumpFlag.equals("ON")) {
            value = true;
        }
        vm.setDump(value);
    }

    @Override
    public String toString() {
        return "DUMP " + dumpFlag;
    }
    
}
