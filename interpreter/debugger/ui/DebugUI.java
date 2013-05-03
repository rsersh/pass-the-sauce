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
    
    
    private DebugUI() {}
   
    
    static void displayMenu() {
        System.out.println("Help Menu of Commands");
        System.out.println("*********************");
        System.out.println("?\thelp");
        System.out.println("s #(s)\tset breakpoint at line#(s)");
        System.out.println("\t- separate multiple line#s with a single space:" 
                + " s 4 8 10");
        System.out.println("x #(s)\tclear breakpoint at line#");
        System.out.println("\t- separate multiple line#s with a single space:"
                + " x 4 8 10");
        System.out.println("l\tlist lines that have breakpoints set");
        System.out.println("o\tstep out of the current function");
        System.out.println("f\tdisplay current function");
        System.out.println("c\tcontinue execution");
        System.out.println("q\tquit execution");
        System.out.println("v\tdisplay variable(s)");
        System.out.println("*********************");
        //promptAndProcess();
    }
    
    public static void promptAndProcess(DebugVM vm) {
        dvm = vm;
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        displayMenu();
        System.out.println();
        displayFunction();

        dvm.setToRun();
        //System.out.println("Is the VM set to run?  " + dvm.isRunning());
        try {
            
            while (dvm.isRunning()) {
        
               System.out.println("Type ? for help");
               System.out.println("> ");    
        
               String line = buff.readLine();
               String[] command = line.split(" ");
               //process command
               /*
               int size = command.length;
               for (int i = 0; i < size; i++) {
                   System.out.println("Command[" + i + "] = " + command[i]);
               }*/
               if (command[0].equals("c")) {
                  continueExecution();
                   //dvm.executeProgram();
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
              }
              else if (command[0].equals("o")) {
                  //step out and then
                  stepOut();;
              } 
              
              else if (command[0].equals("s")) {
                  if (command.length < 2) {
                      System.out.println("ERROR: Insufficient input.");
                      System.out.println();
                      displayMenu();
                  } else {
                      setBreakPoint(command);
                  }
              } else if (command[0].equals("x")) {
                  if (command.length < 2) {
                      System.out.println("ERROR: Insufficient input.");
                      System.out.println();
                      displayMenu();
                  } else {
                      clearBreakPoint(command);
                  }
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
    
    static void showBreakList() {
        System.out.println("Breaks are set at lines: " + dvm.showBreaks());
    }

    static void setBreakPoint (String[] lineNums) {
        int size = lineNums.length;
        for (int i = 1; i < size; i++) {
            int number = Integer.parseInt(lineNums[i]);
            dvm.setBreak(number);
        }
    }
    
    static void clearBreakPoint (String[] lineNums) {
        int size = lineNums.length;
        for (int i = 1; i < size; i++) {
            int number = Integer.parseInt(lineNums[i]);
            dvm.clearBreak(number);
        }
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
    
    static void displayFunction() {
        System.out.println("\n" + dvm.displayFunc());
    }
    
    static void displayVariables() {
        System.out.println(dvm.displayVars());
    }
}

