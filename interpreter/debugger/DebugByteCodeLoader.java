package interpreter.debugger;

import interpreter.CodeTable;
import interpreter.Program;
import interpreter.bytecodes.ByteCode;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Rachel Sershon
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
    
    String getCodeClass(String code) {
        String codeClass = CodeTable.get(code);
        if (isDebuggerByteCode(codeClass)) {
            return "interpreter.bytecodes.debuggerbytecodes." + codeClass;
        }
        
        return "interpreter.bytecodes." + codeClass;
    }
    
    /*
    public Program loadCodes() {
        sourceProgram = new Program();
        String code;
        StringTokenizer line;
        ByteCode bytecode;
        
        try {
            while (source.ready()) {
                line = new StringTokenizer(source.readLine());
                code = line.nextToken();
                Vector<String> args = new Vector<String>();
                while (line.hasMoreTokens()) {    
                    args.add(line.nextToken());   
                }
                String codeClass = CodeTable.get(code);
                
                if (isDebuggerByteCode(codeClass)) {
                  try {
                    bytecode = 
                     (ByteCode)(Class.forName("interpreter.bytecodes.debuggerbytecodes."
                          +codeClass).newInstance());
                    bytecode.init(args);
                    sourceProgram.addCode(bytecode);
                  } catch (Exception e) {
                    System.out.println("ERROR : Class not found.");
                  }  
                }
                
            try {
               bytecode = 
                 (ByteCode)(Class.forName("interpreter.bytecodes."+codeClass).newInstance());
               bytecode.init(args);
               sourceProgram.addCode(bytecode);
                
            } catch (Exception e) {
                System.out.println("ERROR : Class not found.");
            }            
          }
        
        } catch (IOException ex) {
            System.out.println("ERROR: Problem with reading source. ");
        }

        sourceProgram.resolveCodes();
        return sourceProgram;
    }
    */
    
    
}
