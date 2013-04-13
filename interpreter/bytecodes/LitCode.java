/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs virtual machine to push the given value to the top of the runstack.
 */
public class LitCode extends ByteCode {

    private String name = "LitCode";
    private int value;
    private String id;
    
    @Override
    public void init(Vector<String> args) {
        String temp = args.get(0);
        value = Integer.parseInt(temp);
        if (args.size() > 1) {
            id = args.get(1);
        }
    }  

    @Override
    public void execute(VirtualMachine vm) {
        //load the literal value n 
        vm.pushRunStack(value);
    }

    @Override
    public String getArgs() {
        String args = "";
        args += " value: " + value + "   id:  " + id;
        return args;
    }

    @Override
    public String toString() {
        String enchilada =  "LIT " + value;
        if (id != null) {
            enchilada += " " + id + "\t   int " + id;
        }
        return enchilada;
   }
}   