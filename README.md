# Emerald

Emerald is a stack-based programming language designed for simplicity. It's name is a play on the language Ruby, though it is not similar.

| Instruction    | Description                                                                 |
| -------------- | --------------------------------------------------------------------------- |
| `PUSH_NUM`     | Push a number onto the stack.                                                |
| `PUSH_STR`     | Push a string onto the stack.                                                |
| `ADD`          | Pops two numbers from the stack, adds them, and pushes the result back.     |
| `SUBTRACT`     | Pops two numbers from the stack, subtracts the second from the first, and pushes the result. |
| `MUL`          | Pops two numbers from the stack, multiplies them, and pushes the result.   |
| `DIV`          | Pops two numbers from the stack, divides the first by the second, and pushes the result. |
| `REPEAT`       | Pops a string and a count from the stack, prints the string the given number of the count. |
| `OUTPUT`       | Pops the top value from the stack and prints it.                             |
| `ACQUIRE_INPUT`| Takes user input from the terminal and pushes it onto the stack. If left empty, pushes `0` onto the stack. |
| `SET_VAR`      | Pops a value from the stack and stores it in a variable.                     |
| `GET_VAR`      | Retrieves the value of a variable and pushes the value onto the stack.             |
| `COMBINE`      | Pops two values of the same type and combines them. For strings, it concatenates, for numbers, it adds. |
| `HALT`         | Terminates the program execution.                                           |

---

### Here are some examples of code inputs and outputs:


`Examples/helloworld.emd`

**Input:**

PUSH_STR Hello World!

OUTPUT

**Output:**

Hello Word!


`Examples/repeater.emd`

**Input:**

PUSH_STR Enter message to be repeated: 

OUTPUT

ACQUIRE_INPUT

SET_VAR msg


PUSH_STR Enter times to be repeated:

OUTPUT

ACQUIRE_INPUT

SET_VAR times



GET_VAR times

GET_VAR msg


REPEAT

**Output:**

Enter message to be repeated: Hello World!

Enter times to be repeated: 3

Hello World! Hello World! Hello World!
