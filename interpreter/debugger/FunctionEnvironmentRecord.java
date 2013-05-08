/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */

package interpreter.debugger;

//import interpreter.debugger.SymbolTable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

/**
 * Maintains a record of functions variables and values. 
 *  
 */
public class FunctionEnvironmentRecord {
    
    SymbolTable symtab;
    int startLine;
    int endLine;
    int currentLine;
    String name;
    
    public FunctionEnvironmentRecord() {
        symtab = new SymbolTable(); 
        symtab.beginScope();
    }
    
    /**
     * Sets the start line of the function.
     * @param lineno - the line number
     */
    public void setStartLine(int lineno) {
        startLine = lineno;
    }
    
    /**
     * Sets the end line of the function.
     * @param lineno - the line number
     */
    public void setEndLine(int lineno) {
        endLine = lineno;
    }
    
    /**
     * Sets the current line executing in the function.
     * @param lineno - the line number
     */
    public void setCurrentLine(int lineno) {
        currentLine = lineno;
    }
    
    
    /**
     * Sets the name of the function.
     * @param newName - name of the function
     */
    public void setFunctionName(String newName) {
        name = newName;
    }
    
    /**
     * Gets the start line of the function.
     * @return function start line
     */
    public int getStartLine() {
        return startLine;
    }
    
    /**
     * Gets the end line of the function.
     * @return function end line
     */
    public int getEndLine() {
        return endLine;
    }
    
    /**
     * Gets the current line executing in the function.
     * @return current line in function
     */
    public int getCurrentLine() {
        return currentLine;
    }
    
    /**
     * Gets the name of the function.
     * @return function name
     */
    public String getName() {
        return name; 
    }
    
    
    /**
     * Enters an id/offset pair into the Symbol Table.
     * @param var  - the id
     * @param offset  - the offset of the variable in the stack
     */
    public void enterPair(String var, int offset) {
        symtab.put(var, offset);
    }
    
    /**
     * Removes the last n id/offset pairs from the Symbol Table.
     * @param n - the number of pairs to remove from the Symbol Table
     */
    public void popPairs(int n) {
        symtab.endScope(n);
    }
     
    String dumpFER() {
        String forprint = "";
        forprint += "(<" + symtab.printBindings() + ">, " + name + ", ";
        if (startLine == 0) {
            forprint += "-, ";
        } else {
            forprint += startLine + ", ";
        } 
        if (endLine == 0) {
            forprint += "-, ";
        } else {
            forprint += endLine + ", ";
        }
        if (currentLine == 0) {
            forprint += "-)";
        } else {
            forprint += currentLine + ")";
        }
        return forprint;
    }
    
    
    
    /**
     *
     * @return a string of id's in the Symbol Table.
     */
    public Set<String> getVars() {
        return symtab.keys();
    }
    
    /*
    public static void main(String args[]) {
        System.out.println("BS");
        FunctionEnvironmentRecord fer = new FunctionEnvironmentRecord();
        System.out.println("setFunction('g', 1, 20)");
        fer.setFunction("g", 1, 20);
        System.out.println(fer.dumpFER());
        System.out.println("setCurrentLine(5)");
        fer.setCurrentLine(5);
        System.out.println(fer.dumpFER());
        System.out.println("enterPair('a',4)");
        fer.enterPair("a", 4);
        System.out.println(fer.dumpFER());
        System.out.println("enterPair('b',2)");
        fer.enterPair("b", 2);
        System.out.println(fer.dumpFER());
        System.out.println("enterPair('c',7)");
        fer.enterPair("c", 7);
        System.out.println(fer.dumpFER());
        System.out.println("enterPair('a',1)");
        fer.enterPair("a", 1);
        System.out.println(fer.dumpFER());
        System.out.println("popPairs(2)");
        fer.popPairs(2);
        System.out.println(fer.dumpFER());
        System.out.println("popPairs(1)");
        fer.popPairs(1);
        
    }
    */
}

class Binder {
    private Object value;
    private String prevtop;   // prior symbol in same scope
    private Binder tail;      // prior binder for same symbol
                            // restore this when closing scope
    Binder(Object v, String p, Binder t) {
	value=v; prevtop=p; tail=t;
    }

    Object getValue() { return value; }
    String getPrevtop() { return prevtop; }
    Binder getTail() { return tail; }
}

class SymbolTable {

  private java.util.HashMap<String,Binder> symbols = new java.util.HashMap<String,Binder>();
  private String top; // reference to last symbol added to
                         // current scope; this essentially is the
                         // start of a linked list of symbols in scope

  public SymbolTable(){}

  /**
    * Gets the object associated with the specified symbol in the Table.
    */
  public Object get(String key) {
    Binder e = symbols.get(key);
    return e.getValue();
  }

 /**
  * Puts the specified value into the Table, bound to the specified Symbol.<br>
  * Maintain the list of symbols in the current scope (top);<br>
  * Add to list of symbols in prior scope with the same string identifier
  */
  public void put(String key, Object value) {
    symbols.put(key, new Binder(value, top, symbols.get(key)));
    top = key;
  }

 /**
  * Remembers the current state of the Table; push new mark on mark stack
  */
  public void beginScope() {
    top=null;
  }

 /**
  * Restores the table to what it was at the most recent beginScope
  * that has not already been ended.
  */
  public void endScope(int numberOfPairs) {
      while (numberOfPairs > 0) {
          Binder e = symbols.get(top);
          if (e.getTail()!=null) 
            symbols.put(top,e.getTail());
          else symbols.remove(top);
          top = e.getPrevtop();
          numberOfPairs--;
      }
  }
  /**
   * @return a set of the Table's symbols.
   */
  public java.util.Set<String> keys() {return symbols.keySet();}
  
  /**
   * @return a string of all binding pairs from current scope 
   */
  public String printBindings() {
    java.util.Set<String> keys = keys();
    int c = 1;
    String bindingList = ""; 
    for( String k : keys ) {
        bindingList += k + "/" + get(k);  
        if (c < keys.size()) {
            bindingList += ",";
        }
        c++;
    }
    return bindingList;
  }
  
}