/**
 * @author Rachel Sershon
 * @version 04-08-2013
 */
package interpreter.bytecodes;

import interpreter.VirtualMachine;
import java.util.Vector;

/**
 * Instructs virtual machine to pop top 2 levels of the stack, 
 * perform the operation indicated by the indicated operator, and
 * instructs virtual machine to push the result.
 */
public class BopCode extends ByteCode {
    
    private String name = "BopCode";
    private String operator;

    @Override
    public void init(Vector<String> args) {
        operator = args.get(0);
    }
    
    @Override
    public void execute(VirtualMachine vm) {
        //pop top 2 levels of the stack and perform indicated operation
        int operand2 = vm.popRunStack();
        int operand1 = vm.popRunStack();

        if (operator.equals("+")) {
            vm.pushRunStack(operand1 + operand2);
        } else if (operator.equals("-")) {
            vm.pushRunStack(operand1 - operand2);
        } else if (operator.equals("/")) {
            vm.pushRunStack(operand1 / operand2);
        } else if (operator.equals("*")) {
            vm.pushRunStack(operand1 * operand2);
        } else if (operator.equals("==")) {
            if (operand1 == operand2) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);  //0 is for false
            }
        } else if (operator.equals("!=")) {
            if (operand1 != operand2) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);
            }
        } else if (operator.equals("<=")) {
            if (operand1 <= operand2) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);
            }
        } else if (operator.equals(">")) {
            if (operand1 > operand2) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);
            }
        } else if (operator.equals(">=")) {
            if (operand1 >= operand2) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);
            }
        } else if (operator.equals("<")) {
            if (operand1 < operand2) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);
            }
        } else if (operator.equals("|")) {
            if ((operand1 == 1) || (operand2 == 1)) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);
            }
        } else if (operator.equals("&")) {
            if ((operand1 == 1) && (operand2 == 1)) {
                vm.pushRunStack(1);
            } else {
                vm.pushRunStack(0);
            }
        }
    
    }

    @Override
    public String getArgs() {
        return operator;
    }
    
    public String toString() {
        return "BOP " + operator;
    }

}
