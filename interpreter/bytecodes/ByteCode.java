/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

public abstract class ByteCode {
    
    /**
     * @return the arguments as a string.  This is used when re-initializing 
     * the bytecode.
     */
    public abstract String getArgs();
    
    /**
     * @param args vector containing any arguments belonging 
     * to the instance bytecode
     */
    public abstract void init(Vector<String> args);
    
    public abstract void execute(VirtualMachine vm);
    
    @Override
    public abstract String toString();
    
}
