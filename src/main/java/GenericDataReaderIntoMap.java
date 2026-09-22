import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GenericDataReaderIntoMap {
    String hashFunction = "Default"; // FIX: String values must be in quotes
    int capacity = 128;
    public SimpleMapHashTableInterface<String, String> keyValuePairs;

    // For display
    public String keyTitle;
    public String valueTitle;

    // FIX: Fixed constructor chaining.
    // The "this(fileName)" must be the first line, but we need to set
    // fields AFTER the call or fix the logic flow.
    public GenericDataReaderIntoMap(String fileName, String hashFunction, int capacity) {
        // Initialize fields before passing to the constructor that uses them
        this.hashFunction = hashFunction;
        this.capacity = capacity;

        // Initialize the map here specifically with these parameters
        this.keyValuePairs = new SimpleMapHashTable<>(this.capacity, this.hashFunction);

        // Now call a common loader method
        loadData(fileName);
    }

    public GenericDataReaderIntoMap(String fileName) {
        // Default constructor
        keyValuePairs = new SimpleMapHashTable<>(capacity, hashFunction);
        loadData(fileName);
    }

    // Extracted logic to avoid code duplication and fix constructor issues
    private void loadData(String fileName) {
        String line;
        int lineno = 0;
        String[] data;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                // Use a more robust CSV splitting method if data contains commas in quotes
                data = line.split(",");

                if (lineno == 0) {
                    // Save the header
                    if (data.length >= 2) {
                        keyTitle = data[0].trim();
                        valueTitle = data[1].trim();
                    }
                } else {
                    if (data.length >= 2) {
                        String keyInput = data[0].trim();
                        // Combine data if comma was inside the value
                        StringBuilder valueBuilder = new StringBuilder();
                        for(int i=1; i<data.length; i++){
                            valueBuilder.append(data[i]);
                            if(i < data.length -1) valueBuilder.append(",");
                        }

                        String valueInput = "Line " + lineno + ": " + valueBuilder.toString().trim();

                        String curValue = keyValuePairs.get(keyInput);
                        if (curValue != null) {
                            // append this to prev
                            valueInput = curValue + "\n" + valueInput;
                        }
                        keyValuePairs.put(keyInput, valueInput);
                    }
                }
                lineno++;
            }
            // Print table info if method exists
            System.out.println("Data loaded. Entries: " + lineno);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Static method for key-value pair lookups
    public void keyValueLookupCommandLine() {
        Scanner symbolscanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a key (" + this.keyTitle + ") or 'quit' to exit: ");
            String key = symbolscanner.nextLine().trim();
            if (key.equalsIgnoreCase("quit")) {
                break;
            }
            String value = this.keyValuePairs.get(key); // Removed improper cast
            if (value != null) {
                System.out.println("Value (" + this.valueTitle + "): " + value);
            } else {
                System.out.println("Key not found.");
            }
        }
        System.out.println("Thank you for using the Key-Value Lookup program");
    }

    public String keyValueLookup(String keyString) {
        String keyStringTrim = keyString.trim();
        return keyValuePairs.get(keyStringTrim);
    }

    public static void test() {
        // Ensure this file exists at this path
        String fileName = "TopSongs5000Edited.csv";
        GenericDataReaderIntoMap ourGenericDataReaderIntoMap = new GenericDataReaderIntoMap(fileName);
        ourGenericDataReaderIntoMap.keyValueLookupCommandLine();
    }

    public static void main(String[] args) {
        test();
    }
}
