/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

/**
 *
 * @author rsersh
 */

      /** <pre>
 *  Binder objects group 3 fields
 *  1. a value
 *  2. the next link in the chain of symbols in the current scope  //symbol needs to be a value with a link??
 *  3. the next link of a previous Binder for the same identifier
 *     in a previous scope
 *  </pre>
*/
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


/** <pre>
 * The Table class is similar to java.util.Dictionary, except that
 * each key must be a Symbol and there is a scope mechanism.
 *
 * Consider the following sequence of events for table t:
 * t.put(Symbol("a"),5)
 * t.beginScope()
 * t.put(Symbol("b"),7)
 * t.put(Symbol("a"),9)
 * 
 * symbols will have the key/value pairs for Symbols "a" and "b" as:
 * 
 * Symbol("a") ->
 *     Binder(9, Symbol("b") , Binder(5, null, null) )
 * (the second field has a reference to the prior Symbol added in this
 * scope; the third field refers to the Binder for the Symbol("a")
 * included in the prior scope)
 * Binder has 2 linked lists - the second field contains list of symbols
 * added to the current scope; the third field contains the list of
 * Binders for the Symbols with the same string id - in this case, "a"
 * 
 * Symbol("b") ->
 *     Binder(7, null, null)
 * (the second field is null since there are no other symbols to link
 * in this scope; the third field is null since there is no Symbol("b")
 * in prior scopes)
 * 
 * top has a reference to Symbol("a") which was the last symbol added
 * to current scope
 * 
 * Note: What happens if a symbol is defined twice in the same scope??
 * </pre>
*/
public class SymbolTable {

  private java.util.HashMap<String,Binder> symbols = new java.util.HashMap<String,Binder>();
  private String top;    // reference to last symbol added to
                         // current scope; this essentially is the
                         // start of a linked list of symbols in scope
  private Binder marks;  // scope mark; essentially we have a stack of
                         // marks - push for new scope; pop when closing
                         // scope

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
    marks = new Binder(null,top,marks);
    top=null;
  }

  
  //may need to later modify for pop below
 /**
  * Restores the table to what it was at the most recent beginScope
  *	that has not already been ended.
  */
  public void endScope() {
	while (top!=null) {
	   Binder e = symbols.get(top);
	   if (e.getTail()!=null) symbols.put(top,e.getTail());
	   else symbols.remove(top);
	   top = e.getPrevtop();
	}
	top=marks.getPrevtop();
	marks=marks.getTail();
  }

  /**
   * @return a set of the Table's symbols.
   */
  public java.util.Set<String> keys() {return symbols.keySet();}
  
} 
/* 
 public static void main(String args[]) {
        Symbol s = Symbol.symbol("a", 1),
        s1 = Symbol.symbol("b", 2),
        s2 = Symbol.symbol("c", 3);

        Table t = new Table();
        t.beginScope();
        t.put(s,"top-level a");
        t.put(s1,"top-level b");
        t.beginScope();
        t.put(s2,"second-level c");
        t.put(s,"second-level a");
        t.endScope();
        t.put(s2,"top-level c");
        t.endScope();
  }
*/
  /*
public static void main(String args[]) {
    BufferedReader source;
    
    FunctionEnvironmentRecord fctEnvRecord = new FunctionEnvironmentRecord();
    if (args.length == 0) {
        System.out.println("Insufficient arguments - need filename");
        System.exit(1);
    }
    String filename = args[0];
    try {
        source = new BufferedReader(new FileReader(filename));
    } 
    catch (IOException e) {  
        System.out.println("ERROR: Problem opening file");
    }
    //grab a line and read command
    //BS
    //Function name start end   -- Function g 1 20
    //Line n   -- Line 5
    //Enter var value   -- Enter a 4   <(<a/4>, g, 1, 20, 5>
    //Enter b 2  <(<a/4, b/2>, g, 1, 20, 5)>
    //Enter c 7 <(<a/4, c/7, b/2>, g, 1, 20, 5)>
    //Enter a 1 <(<a/1, c/7, b/2>, g, 1, 20, 5)>
    //Pop 2  <(<a/4, b/2>, g, 1, 20, 5)>
    //Pop 1  <(<a/4>, g, 1, 20, 5)>
            
   } */     
