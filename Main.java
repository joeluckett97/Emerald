import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Check if the file path argument is provided
        if (args.length < 1) {
            System.out.println("Error: Missing file argument.");
            System.exit(0);
        }

        String fileName = args[0];
        List<String> lines = new ArrayList<>();

        // Read the file and load its contents into lines
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error while opening file:\n" + e);
            System.exit(0);
        }

        // Initialize the interpreter and execute the code
        Interpreter interpreter = new Interpreter(lines);
        interpreter.run();
    }
}
