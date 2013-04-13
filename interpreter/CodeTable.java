/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter;

/**
 * The CodeTable is used by the ByteCodeLoader to provide the appropriate
 * bytecode class.  It implements the Singleton Design Pattern, limiting 
 * construction and initializing the code table through an init method.
 */
public class CodeTable {
   
    private static java.util.HashMap<String, String> codetable = 
            new java.util.HashMap<String, String>();
    
    /**
     * @param code the name of the bytecode (should be in CAPS ex. HALT)
     * @return the class name that matches the bytecode name (ex. HaltCode)
     */
    public static String get(String code) { 
            return codetable.get(code);
    }
    
    public static void init() {
        codetable.put("ARGS", "ArgsCode");
        codetable.put("BOP", "BopCode");
        codetable.put("CALL", "CallCode");
        codetable.put("DUMP", "DumpCode");
        codetable.put("FALSEBRANCH", "FalsebranchCode");
        codetable.put("GOTO", "GotoCode");
        codetable.put("HALT", "HaltCode");
        codetable.put("LABEL", "LabelCode");
        codetable.put("LIT", "LitCode");
        codetable.put("LOAD", "LoadCode");
        codetable.put("POP", "PopCode");
        codetable.put("READ", "ReadCode");
        codetable.put("RETURN", "ReturnCode");
        codetable.put("STORE", "StoreCode");
        codetable.put("WRITE", "WriteCode");
    }
}
