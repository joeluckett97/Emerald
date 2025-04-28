/**
 * Author : Joseph Luckett
 * Interpreter class for a stack-based virtual machine.
 * 
 * This class simulates the execution of a stack-based language. It processes a list of instructions 
 * (in string format) and performs actions based on the commands provided. The interpreter uses a stack 
 * to store and manipulate data, such as numbers and strings, and supports basic arithmetic operations, 
 * input/output handling, variable assignment, and more.
 *
 * The supported commands include:
 * - PUSH_NUM: Push an integer onto the stack.
 * - PUSH_STR: Push a string onto the stack.
 * - INCREMENT: Increment the top number on the stack.
 * - ADD, SUBTRACT, MUL, DIV: Perform arithmetic operations using the top two numbers on the stack.
 * - OUTPUT: Output the top element of the stack to the console.
 * - ACQUIRE_INPUT: Read input from the user and push it onto the stack.
 * - SET_VAR, GET_VAR: Assign a value to a variable or retrieve the value of a variable.
 * - COMBINE: Combine (concatenate) two strings or add two integers.
 * - REPEAT: Repeat the top element on the stack a specified number of times.
 * - REVERSE: Reverse the string at the top of the stack.
 * - HALT: Terminate the program.
 * 
 * The program continues execution until either the HALT command is encountered or all instructions
 * have been processed. Errors are reported with line numbers to help trace issues in the code.
 * 
 * The interpreter manages a stack, a variable map for storing variables, and an instruction pointer
 * that tracks the current execution position within the program.
 * 
 * Each instruction is parsed, and its corresponding action is executed. The interpreter handles 
 * potential errors like stack underflow, division by zero, and invalid operations gracefully.
 */

import java.util.*;

public class Interpreter {
    private List<String> program;
    private Stack<Object> dataStack;
    private int instructionPointer;
    private Map<String, Object> variables = new HashMap<>();  // To store variables

    // Constructor to initialize the program and stack
    public Interpreter(List<String> program) {
        this.program = program;
        this.dataStack = new Stack<>();
        this.instructionPointer = 0;
    }

    // Main interpreter run loop
    public void run() {
        while (instructionPointer >= 0 && instructionPointer < program.size()) {
            String line = program.get(instructionPointer).trim();
            if (line.isEmpty()) {
                instructionPointer++;
                continue;
            }

            String[] tokens = line.split(" ", 2);
            String command = tokens[0];

            // Process each command in the program
            switch (command) {
                // Push number onto the stack
                case "PUSH_NUM":
                    if (tokens.length < 2) triggerError("Missing number for PUSH_NUM");
                    try {
                        int num = Integer.parseInt(tokens[1]);
                        dataStack.push(num);
                    } catch (NumberFormatException e) {
                        triggerError("Invalid number for PUSH_NUM");
                    }
                    break;

                // Push string onto the stack
                case "PUSH_STR":
                    if (tokens.length < 2) triggerError("Missing string for PUSH_STR");
                    dataStack.push(tokens[1]);
                    break;

                // Increment the top number on the stack
                case "INCREMENT":
                    int val = popNumber();
                    dataStack.push(val + 1);
                    break;

                // Add the top two numbers on the stack
                case "ADD":
                    int addB = popNumber();
                    int addA = popNumber();
                    dataStack.push(addA + addB);
                    break;

                // Subtract the top two numbers on the stack
                case "SUBTRACT":
                    int subB = popNumber();
                    int subA = popNumber();
                    dataStack.push(subA - subB);
                    break;

                // Multiply the top two numbers on the stack
                case "MUL":
                    int mulB = popNumber();
                    int mulA = popNumber();
                    dataStack.push(mulA * mulB);
                    break;

                // Divide the top two numbers on the stack
                case "DIV":
                    int divB = popNumber();
                    int divA = popNumber();
                    if (divB == 0) triggerError("Division by zero");
                    dataStack.push(divA / divB);
                    break;

                // Output the top of the stack
                case "OUTPUT":
                    Object out = pop();
                    System.out.print(out.toString());
                    break;

                // Acquire input and push it onto the stack
                case "ACQUIRE_INPUT":
                    try {
                        Scanner scanner = new Scanner(System.in);
                        String input = scanner.nextLine();
                        if (input.isEmpty()) {
                            dataStack.push(0);
                        } else {
                            dataStack.push(input);
                        }
                    } catch (Exception e) {
                        dataStack.push(0);
                    }
                    break;

                // Set a variable to a value from the stack
                case "SET_VAR":
                    if (tokens.length < 2) {
                        triggerError("Missing variable name for SET_VAR");
                    }
                    String varNameForSet = tokens[1];
                    if (!dataStack.isEmpty()) {
                        Object value = pop();

                        // Store value as either an Integer or String
                        if (value instanceof Integer) {
                            variables.put(varNameForSet, (Integer) value);
                        } else if (value instanceof String) {
                            variables.put(varNameForSet, (String) value);
                        } else {
                            triggerError("Unsupported variable type for SET_VAR: " + value.getClass().getSimpleName());
                        }
                    } else {
                        triggerError("Stack is empty, unable to set variable " + varNameForSet);
                    }
                    break;

                // Get the value of a variable and push it onto the stack
                case "GET_VAR":
                    if (tokens.length < 2) triggerError("Missing variable name for GET_VAR");

                    String varNameForGet = tokens[1];

                    if (variables.containsKey(varNameForGet)) {
                        Object varValue = variables.get(varNameForGet);
                        dataStack.push(varValue);
                    } else {
                        triggerError("Variable " + varNameForGet + " not found");
                    }
                    break;

                // Combine two items from the stack (either two strings or two integers)
                case "COMBINE":
                    combine();
                    break;

                // Repeat the top item on the stack for the specified number of times
                case "REPEAT":
                    repeat();
                    break;

                // Reverse the string on top of the stack
                case "REVERSE":
                    reverseString();
                    break;
                    
                 // Halt the program execution
                case "HALT":
                    return;

                // Handle unknown instructions
                default:
                    triggerError("Unknown instruction: " + command);
            }

            // Move to the next instruction
            instructionPointer++;
        }
        System.out.println();
    }

    // Reverse the string on top of the stack
    private void reverseString() {
        if (dataStack.isEmpty()) {
            triggerError("Not enough elements on the stack for REVERSE_STRING");
        }

        Object item = pop(); // The top of the stack is the item to reverse

        if (!(item instanceof String)) {
            triggerError("Item for REVERSE_STRING must be a string or variable holding a string");
        }

        String str = (String) item;
        String reversed = new StringBuilder(str).reverse().toString(); // Reverse the string

        dataStack.push(reversed); // Push the reversed string back onto the stack
    }

    // Combine two items from the stack (either strings or integers)
    private void combine() {
        if (dataStack.size() < 2) {
            triggerError("Not enough elements on the stack for COMBINE");
        }
        Object second = pop();
        Object first = pop();

        if (first instanceof String && second instanceof String) {
            dataStack.push((String) first + (String) second); // Concatenate strings
        } else if (first instanceof Integer && second instanceof Integer) {
            dataStack.push((Integer) first + (Integer) second); // Add integers
        } else {
            triggerError("COMBINE can only operate on two strings or two integers");
        }
    }

    // Repeat the top item on the stack for the specified number of times
    private void repeat() {
        if (dataStack.size() < 2) {
            triggerError("Not enough elements on the stack for REPEAT");
        }

        Object repeatItem = pop(); // The item to repeat
        int repeatCount = popNumber(); // The repeat count

        if (repeatCount < 0) {
            triggerError("Repeat count must be a positive number");
        }

        for (int i = 0; i < repeatCount; i++) {
            System.out.print(repeatItem.toString());
        }
    }

    // Pop an object from the stack
    private Object pop() {
        if (dataStack.isEmpty()) triggerError("Stack underflow encountered");
        return dataStack.pop();
    }

    // Pop a number from the stack
    private int popNumber() {
        Object obj = pop();
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                triggerError("Cannot convert string to number: " + obj);
            }
        }
        triggerError("Expected number on stack but got: " + obj);
        return 0; // Unreachable, but required
    }

    // Trigger an error and halt the program execution
    private void triggerError(String message) {
        System.out.println("\n" + message + " at line " + (instructionPointer + 1));
        System.exit(0);
    }
}
