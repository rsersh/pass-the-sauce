package interpreter.debugger.ui;

import interpreter.debugger.DebugVM;

/**
 *
 * @author Rachel Sershon
 */
public class DebugUI {
    
    DebugVM dvm;
    
    public DebugUI(DebugVM debugVM) {
        dvm = debugVM;
        displayMenu();
    }
    
    void displayMenu() {
        System.out.println("?\thelp");
        System.out.println("s line#\t set breakpoint at line#");
        promptAndProcess();
    }
    
    void promptAndProcess() {
        System.out.println("Type ? for help");
        //grab response and process/execute command
        //if (s or x) check for second arg/entry
        //command is a string array
        //process command (char or char int)
    }

    void setBreakPoint(int lineno) {
        dvm.setBreak(lineno);
    }
    
    
}

