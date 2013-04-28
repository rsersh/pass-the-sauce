package interpreter.debugger.ui;

import interpreter.debugger.DebugVM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rachel Sershon
 */
public class DebugUI {
    
    static DebugVM dvm;
    
    
    public DebugUI(DebugVM debugVM) {
        dvm = debugVM;
        //displayMenu();
    }
   
    
    void displayMenu() {
        System.out.println("Help Menu of Commands");
        System.out.println("*********************");
        System.out.println("?\thelp");
        System.out.println("s #(s)\tset breakpoint at line#(s)");
        System.out.println("\t- separate multiple line#s with a single space:" 
                + " s 4 8 10");
        System.out.println("x #(s)\tclear breakpoint at line#");
        System.out.println("\t- separate multiple line#s with a single space:"
                + "x 4 8 10");
        System.out.println("f\tdisplay current function");
        System.out.println("c\tcontinue execution");
        System.out.println("q\tquit execution");
        System.out.println("v\tdisplay variable(s)");
        //promptAndProcess();
    }
    
    void promptAndProcess() {
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type ? for help");
        System.out.println("> ");
        try {
            String line = buff.readLine();
            String[] command = line.split(" ");

            if (command[0].equals("c")) {
                dvm.executeProgram();
            } else if (command[0].equals("q")) {
                dvm.quit();
            } else if (command[0].equals("f")) {
                //System.out.println("display current function");
                displayFunction();
            } else if (command[0].equals("v")) {
                //System.out.println("Display variables");
                displayVariables();
            } else if (command[0].equals("?")) {
                displayMenu();
            } else {
                if (command.length < 2) {
                   System.out.println("ERROR: Insufficient input");
                   displayMenu();
                } 
                if (command[0].equals("s")) {
                    setBreakPoint(command);
                } //command[0] should equal x
                if (command[0].equals("x")) {
                    clearBreakPoint(command);
                } else {
                    System.out.println("ERROR: Problem processing command");
                    displayMenu();
                }
            }     
        } catch (IOException e) {
            System.out.println("ERROR: Problem reading user input.");
            System.exit(1);
        }
        
    }
   
    public void execute() {
        
        displayFunction();
        while (dvm.isRunning()) {
            promptAndProcess();
        }
        
    }
    /*
    executeVM() {
    }
    */

    void setBreakPoint (String[] lineNums) {
        int size = lineNums.length;
        for (int i = 1; i < size; i++) {
            int number = Integer.parseInt(lineNums[i]);
            dvm.setBreak(number);
        }
    }
    
    void clearBreakPoint (String[] lineNums) {
        int size = lineNums.length;
        for (int i = 1; i < size; i++) {
            int number = Integer.parseInt(lineNums[i]);
            dvm.clearBreak(number);
        }
    }
    
    void displayFunction() {
        System.out.println(dvm.displayFunc());
    }
    
    void displayVariables() {
        System.out.println(dvm.displayVars());
    }
}

