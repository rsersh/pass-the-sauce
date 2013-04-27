/** <pre>
 * 
 * 
 * Interpreter class runs the interpreter:
 * 1. Perform all initializations
 * 2. Load the bytecodes from file
 * 3. Run the virtual machine.
 * 
 * 
 * </pre>
 */

package interpreter;

import interpreter.debugger.DebugByteCodeLoader;
import interpreter.debugger.DebugVM;
import interpreter.debugger.SourceCodeLoader;
import interpreter.debugger.SourceLineEntry;
import java.io.*;
import java.util.Vector;

public class Interpreter {

        ByteCodeLoader bcl;
        Boolean debugSet = false;
        Vector<SourceLineEntry> sourceEntries;
        
        /**
         * @param filename name of file without extension
         */
        public Interpreter(String filename, Boolean debugFlag)  {
            debugSet = debugFlag;
            
            if (debugSet) {
                try {
                  CodeTable.init();
                  bcl = new DebugByteCodeLoader(filename + ".x.cod"); 
                  sourceEntries = (new SourceCodeLoader(filename+".x")).loadEntries();
                  
                } catch (IOException e)  {
                  System.out.println("****" + e);
                }
            }
            else {
                try {
                  CodeTable.init();
                  bcl = new ByteCodeLoader(filename + ".x.cod");   
                } catch (IOException e)  {
                  System.out.println("****" + e);
                }
            }
        }
        
        /**
         * Creates a VirtualMachine object to execute the program.
         */
        public void run() {
            
            if (debugSet) {
                Program program = bcl.loadCodes();
                VirtualMachine vm = new DebugVM(program, sourceEntries);
            }
            
            else {
                Program program = bcl.loadCodes();
                VirtualMachine vm = new VirtualMachine(program);
                vm.executeProgram();
            }
        }
    
        public static void main (String args[]) {
            if(args.length == 0)  {
                    System.out.println("***Incorrect usage, try: java interpreter.Interpreter<file>"
                            + "or java -d interpreter.Interpreter<file>");
                    System.exit(1);
            }
            if(args.length==2) {
                if(args[0].equals("-d")) {
                    (new Interpreter(args[1], true)).run();
                } else {
                    System.out.println("***Incorrect usage, try: java interpreter.Interpreter<file>"
                            + " or java -d interpreter.Interpreter<file>");
                    System.exit(1);
                }
            }
            (new Interpreter(args[0], false)).run();
        }
    
}
