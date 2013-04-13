/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.debugger;

import interpreter.debugger.SymbolTable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author rsersh
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
        //enter the var/value pair in the symbol table
    }
    
    void popPairs(int n) {
        //remove the last n var/value pairs from the symbol table
    }
    
    void beginScope() {
        //make sure it's a block, decl, assign, if, while, return
        //set intial values
    }
    
    void endScope() {
        //we should remove the LAST 5 items entered in the symbol table
        //use code similar to that found in the endScope method to remove
        //those symbols
    }
  
  
 
}