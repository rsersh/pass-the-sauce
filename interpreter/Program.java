/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */

package interpreter;

import interpreter.bytecodes.ByteCode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * The Program class holds the bytecode program
 * loaded from the source file and is capable of 
 * resolving symbolic addresses in the program.
 */
public class Program {
    
    private ArrayList<ByteCode> loadedByteCodes = new ArrayList<ByteCode>();
    private ArrayList<Integer> toResolveList = new ArrayList<Integer>();
    private HashMap<String, Integer> labelList = new HashMap<String, Integer>();
    private Integer pc = 0;
    
    /**
     * @param pc  a line number in the loaded source program
     * @return the ByteCode at the given line number
     */
    public ByteCode getCode(int pc) {
        return loadedByteCodes.get(pc);
    }
    
    /**
     * @param label to resolve with targetLocation
     * @return address for symbolic label for re-initialization of ByteCode
     */
    public String getTargetAddress(String label) {
        Integer pc = labelList.get(label);
        return pc.toString();
    } 
    
    /**
     * @param bytecode   bytecode to add to program
     * @return true, if bytecode was successfully added to program
     */
    public Boolean addCode(ByteCode bytecode) {
        Boolean added = false;
        added = loadedByteCodes.add(bytecode);
        
        Class codeClass = bytecode.getClass();
        String codeName = codeClass.getName();
        String[] className = codeName.split("\\.");
        String name = className[2];

        //for resolveCodes later
        if (name.equals("LabelCode")) {
            String label = bytecode.getArgs();
            labelList.put(label, pc);
        }
        //note branching codes for symbolic resolution later
        if (name.equals("CallCode") || name.equals("FalsebranchCode")
            || name.equals("GotoCode") ){ 
            toResolveList.add(pc);
        }
        pc++;
        return added;
    }
    
    /**
     * @return the number of ByteCodes added to the program
     */
    public int size() {
        return loadedByteCodes.size();
    }
    
    
    /**
     * For printing Program of loaded ByteCodes.
     */
    
    public void printProgram() {
        int size = loadedByteCodes.size();
        for(int i = 0; i < size; i++) {
            ByteCode bc = loadedByteCodes.get(i);
            String n = bc.getClass().getName();
            String[] fullName = n.split("\\.");
            String name = fullName[2];
            System.out.println("" + name +  "  /  " + bc.getArgs());
        }
    }
    
    
    /*
     * resolveCodes() grabs the code at the index from the toResolveList. 
     * Gets the location from the labelList HashMap. Creates new vector
     * of arguments, including targetAddress of label and re-initializes.
     */
    public void resolveCodes() {

        for (int i = 0; i < toResolveList.size(); i++) {
            Integer temp = toResolveList.get(i);
            int index = temp.intValue();
            ByteCode code = loadedByteCodes.get(index);
            String label = code.getArgs();
            String address = getTargetAddress(label);
            Vector<String> updatedArgs = new Vector<String>();
            updatedArgs.add(label);
            updatedArgs.add(address);
            code.init(updatedArgs);
        }
    } 
}
