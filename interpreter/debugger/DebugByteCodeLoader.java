/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */

package interpreter.debugger;

import interpreter.CodeTable;
import interpreter.Program;
import interpreter.bytecodes.ByteCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * The DebugByteCodeLoader class extends the ByteCodeLoader
 * class to include the bytecodes required for debugging. 
 * When the bytecodes are loaded we'll get the string for 
 * the bytecode and then we'll get the bytecode class for
 * that string from the Codetable to construct an
 * instance of the bytecode and then store it in the 
 * Program object.
 */
public class DebugByteCodeLoader extends interpreter.ByteCodeLoader {
    
    //Program sourceProgram;
    //public BufferedReader source;
    
    public DebugByteCodeLoader(String filename) throws IOException {
            super(filename);     
    }

    Boolean isDebuggerByteCode(String codeName) {
        Boolean isForDebugger = false;
        if ( codeName.equals("LineCode") || codeName.equals("FormalCode")
         || codeName.equals("FunctionCode") || codeName.equals("LitCode")
         || codeName.equals("PopCode") || codeName.equals("ReturnCode")) {
            isForDebugger = true;
        }
        return isForDebugger;
    }
    
    public String getCodeClass(String code) {
        String codeClass = CodeTable.get(code);
        
        if (isDebuggerByteCode(codeClass)) {
            return "interpreter.bytecodes.debuggerbytecodes." + codeClass;
        }
        
        return "interpreter.bytecodes." + codeClass;
    }   
}
