/**
 * @author Rachel Sershon
 * @version 05-08-2013
 */

package interpreter.debugger.ui;

import interpreter.debugger.DebugVM;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Interface between user and virtual machine executing in debug mode.
 * 
 */
public class DebugUI {
    
    static DebugVM dvm;
    
    //only one instance of the UI is to be created.
    private DebugUI() {}
   
    /**
     * Displays command options to user.
     */
    public static void displayMenu() {
        System.out.println("Help Menu of Commands");
        System.out.println("*********************");
        System.out.println("?\thelp");
        System.out.println("c\tcontinue execution");
        System.out.println("f\tdisplay current function");
        System.out.println("l\tlist lines that have breakpoints set");
        System.out.println("o\tstep out of the current function");
        System.out.println("p\tprint call stack");
        System.out.println("q\tquit execution");
        System.out.println("r\tstep over the nextline");
        System.out.println("s #(s)\tset breakpoint at line#(s)");
        System.out.println("\t- separate multiple line#s with a single space:" 
                + " s 4 8 10");
        System.out.println("t\t set function tracing");
        System.out.println("v\tdisplay variable(s)");
        System.out.println("x #(s)\tclear breakpoint at line#");
        System.out.println("\t- separate multiple line#s with a single space:"
                + " x 4 8 10");
        System.out.println("*********************");

    }
    
    /**
     * Prompts user for command and executes.
     * 
     */
    public static void promptAndProcess(DebugVM vm) {
        dvm = vm;
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        displayMenu();
        System.out.println();
        displayFunction();

        dvm.setToRun();

        try {
            while (dvm.isRunning()) {
               System.out.println("Type ? for help");
               System.out.println("> ");    
        
               String line = buff.readLine();
               String[] command = line.split(" ");
               
               //process command
               if (command[0].equals("c")) {
                  continueExecution();
               } else if (command[0].equals("q")) {
                  dvm.quit();
               } else if (command[0].equals("f")) {
                  displayFunction();
               } else if (command[0].equals("v")) {
                  displayVariables();
              } else if (command[0].equals("?")) {
                  displayMenu();
              } else if (command[0].equals("l")) {
                  showBreakList();
              } else if (command[0].equals("i")) {
                  stepIn();
              } else if (command[0].equals("o")) {
                  stepOut();
              } else if (command[0].equals("r")) {
                  stepOver();
              } else if (command[0].equals("s")) {
                  if (command.length < 2) {
                      System.out.println("ERROR: Insufficient input.");
                      System.out.println();
                      displayMenu();
                  } else {
                      String linesSet = setBreakPoint(command);
                      System.out.println(linesSet);
                  }
              } else if (command[0].equals("x")) {
                  if (command.length < 2) {
                      System.out.println("ERROR: Insufficient input.");
                      System.out.println();
                      displayMenu();
                  } else {
                      String linesCleared = clearBreakPoint(command);
                      System.out.println("Breakpoints cleared at: " + linesCleared);
                  }
              } else if (command[0].equals("t")) {
                  traceOn();
              } else if (command[0].equals("p")) {
                  printCallStack();
              } else {
                  System.out.println("ERROR: Problem reading input. Try again.");
                  displayMenu();
              } 
            }
        } catch (IOException e) {
            System.out.println("ERROR: Problem reading user input.");
            System.exit(1);
        }
    }
    
    //Displays list of line numbers with breakpoints
    private static void showBreakList() {
        System.out.println("Breaks are set at lines: " + dvm.showBreaks());
    }

    private static String setBreakPoint (String[] lineNums) {
        int size = lineNums.length;
        String returnString = "";
        for (int i = 1; i < size; i++) {
            int number = Integer.parseInt(lineNums[i]);
            returnString = dvm.setBreak(number);
        }
        if (!returnString.isEmpty()) {
            return "Breakpoint(s) set at: " + dvm.showBreaks();
        } else {
            return "No breakpoints set.";
        }
     }
    
    static String clearBreakPoint (String[] lineNums) {
        int size = lineNums.length;
        String linesCleared = "";
        for (int i = 1; i < size; i++) {
            int number = Integer.parseInt(lineNums[i]);

            if (dvm.clearBreak(number)) {
                linesCleared += lineNums[i] + " ";
            } else {
                linesCleared += "Error clearing breakpoint on line " + lineNums[i];
            }
        }
        return linesCleared;
    }
    
    static void continueExecution() {
        dvm.continueExecuting();
    }
    
    static void stepIn() {
        dvm.stepIn();
    }
    
    static void stepOut() {
        dvm.stepOut();
    }
    
    static void stepOver() {
        dvm.stepOver();
    }
    
    static void traceOn() {
        dvm.trace();
    }
    
    static void printCallStack() {
        dvm.printCallStack();
    }
    
    static void displayFunction() {
        System.out.println("\n" + dvm.displayFunc());
    }
    
    static void displayVariables() {
        System.out.println(dvm.displayVars());
    }
}