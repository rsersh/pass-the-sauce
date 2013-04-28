package interpreter.debugger;

//import interpreter.debugger.SymbolTable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Rachel Sershon
 */
public class FunctionEnvironmentRecord {
    
    SymbolTable symtab;
    int startLine;
    int endLine;
    int currentLine;
    String name;
    
    FunctionEnvironmentRecord() {
        symtab = new SymbolTable(); 
        symtab.beginScope();
        //System.out.println(dumpFER());
    }
    
    void setStartLine(int lineno) {
        startLine = lineno;
    }
    
    void setEndLine(int lineno) {
        endLine = lineno;
    }
    
    void setCurrentLine(int lineno) {
        currentLine = lineno;
    }
    
    void setFunctionName(String newName) {
        name = newName;
    }

    //for purposes of unit testing
    void setFunction(String n, int start, int end) {
        setFunctionName(n);
        setStartLine(start);
        setEndLine(end);
    }
    
    int getStartLine() {
        return startLine;
    }
    
    int getEndLine() {
        return endLine;
    }
    
    int getCurrentLine() {
        return currentLine;
    }
    
    String getName() {
        return name; 
    }
    
    void enterPair(String var, int value) {
        symtab.put(var, value);
    }
    
    //remove the last n var/value pairs from the symbol table
    void popPairs(int n) {
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