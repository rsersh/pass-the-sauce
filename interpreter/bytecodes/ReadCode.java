/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import interpreter.bytecodes.ByteCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Prompts the user for input. Reads in value and instructs virtual machine
 * to add value to top of stack.
 */
public class ReadCode extends ByteCode {
    
    private String name = "ReadCode";
    private int targetAddress;

    @Override
    public void execute(VirtualMachine vm) {
        //prompt the user for input, put 
        //the value just read on top of the stack
        System.out.println("Enter an integer: ");
        try {
            InputStreamReader innie = new InputStreamReader(System.in);
            BufferedReader buffer = new BufferedReader(innie);
            String line = buffer.readLine();
            int value = Integer.parseInt(line);
            vm.pushRunStack(value);
        } catch (IOException e) {
            System.out.println("ERROR: Problem reading user input");
            System.exit(1);
        }
    }

    @Override
    public String getArgs() {
        //provided to help resolve addresses
        return "Read";
    }

    @Override
    public void init(Vector<String> args) {
        if (args.size() > 0) {
            targetAddress = Integer.parseInt(args.get(1));
        }
    }

    @Override
    public String toString() {
        return "READ ";
    }

}
