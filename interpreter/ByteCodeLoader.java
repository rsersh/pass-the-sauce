/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */

package interpreter;

import interpreter.Program;
import interpreter.bytecodes.ByteCode;
import java.io.*;
import java.util.*;

/**
 * The ByteCodeLoader class will load the bytecodes 
 * from the file into the Virtual Machine.  When the 
 * bytecodes are loaded we'll get the string for the
 * bytecode and then we'll get the bytecode class for
 * that string from the Codetable to construct an
 * instance of the bytecode and then store it in the 
 * Program object.
 * 
 */

public class ByteCodeLoader {
    
    private BufferedReader source;
    private Program sourceProgram;
    
    /**
     * @param programFile the source file
     */
    public ByteCodeLoader(String programFile) throws IOException {
        try {
            source = new BufferedReader(new FileReader(programFile));
        } 
        catch (IOException e) {  
            System.out.println("ERROR: Problem opening file");
        }   
    }

    /**
     * The source code is read line by line and parsed for bytecode name and
     * arguments.  After the appropriate bytecode object is created and 
     * initialized it is added to a Program object.  Prior to return, 
     * the bytecodes are resolved with their symbolic addresses.
     * @return a Program object loaded with the bytecodes
     */
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
}
