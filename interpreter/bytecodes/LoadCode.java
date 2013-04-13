/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.util.Vector;

/**
 * Instructs the virtual machine to push the value at index to the top 
 * of the stack.
 */
public class LoadCode extends ByteCode {

    private String name = "LoadCode";
    private int index;
    private String id;
    
    public String getArgs() {  
        String args = "";
        args += "index: " + index + "  id: " + id;
        return args;  }
    
    @Override
    public void init(Vector<String> args) {
        if(args.size() < 2) {
            System.out.println("Insufficient args for LoadCode");
            System.exit(1);
        }
        String temp = args.get(0);
        index = Integer.parseInt(temp);
        id = args.get(1);
    }   

    @Override
    public void execute(VirtualMachine vm) {
        //push the value in the slot which is offset n
        //from the start of the frame onto the top of the stack
        vm.load(index);
    } 

    @Override
    public String toString() {
        return "LOAD " + index + " " + id + "    <load " + id + ">";
    }
}
