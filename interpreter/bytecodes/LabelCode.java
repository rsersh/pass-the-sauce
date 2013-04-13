/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Contains target address for branch codes.
 */
public class LabelCode extends ByteCode {
    
    private String name = "LabelCode";
    private String label;

    @Override
    public void init(Vector<String> args) {
        label = args.get(0);
    }

    @Override
    public void execute(VirtualMachine vm) {
        
    }

    @Override
    public String getArgs() {
        return label;
    } 

    @Override
    public String toString() {
        return "LABEL " + label;
    }
    
}