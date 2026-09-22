public class SimpleMapHashTableFunctions {

    public static <K> int runHashFunction(String functionName, K key, int capacity) {

        // We will focus on String type
        // to make this work with our GenericDataReaderIntoMap
        if (key instanceof String) {
            switch (functionName) {
                case "StringLength":
                    // Cast to String to reuse
                    String thisString = (String) key;
                    int thishashcode = 0;
                    // TODO: put code here to compute hashcode based on string length
                    // remainder operation by capacity
                    return thishashcode;
                case "CyclicShiftExample":
                    // TODO: Use book code from Chapter 10
                    // Cyclic shift hash codes
                    // Cast to String to make sure
                    // Be sure to abs and mod it: return Math.abs(h) % capacity;
                case "DJB2":
                    thisString = (String) key;
                    // Use example from DJB2: https://share.google/aimode/8zdVMPTc4POpvUTul
                    long hash = 5381;
                    for (int i = 0; i < thisString.length(); i++) {
                        // hash * 33 + character code
                        // (hash << 5) is a fast way to write hash * 32
                        hash = ((hash << 5) + hash) + thisString.charAt(i);
                        // Use bitwise AND with 0x7FFFFFFF to force the number to be positive
                        // before applying modulo, ensuring a valid array index.
                    }
                    return (int) (hash & 0x7FFFFFFF) % capacity;
                case "PolynomialHash":
                    if (key == null || capacity <= 0) {
                        throw new IllegalArgumentException("Invalid input or array size.");
                    }
                    thisString = (String) key;
                    // Use example from: https://share.google/aimode/Z06Rl6bsThCSNls6K
                    // Start from hash = 0
                    hash = 0;
                    // A small prime number is commonly used as the base (31 is standard in Java)
                    long primeBase = 31;
                    for (int i = 0; i < ((String) thisString).length(); i++) {
                        // Standard polynomial formula: h = (h * base) + charValue
                        hash = (hash * primeBase) + thisString.charAt(i);
                    }
                    // Use Math.floorMod to handle potential negative overflows and
                    // ensure the result is always within [0, arraySize - 1]
                    return (int) Math.floorMod(hash, capacity);
                case "Default":
                default:
                    // Use built-in object hashCode() for the "Default" hash function
                    // Math.abs handles negative hash codes, % capacity ensures it fits in the array
                    return 0;
                    //return Math.abs(key.hashCode()) % capacity;
            }
        }

        // Will leave this as previous default for now
        if (key instanceof Integer) {
            int inputkey = (int) key;
            // Hashfunction - use the key itself mod the capacity
            // to prevent array overrun
            // use Math.abs to allow negative numbers
            return Math.abs(inputkey) % capacity;
        }

        // DEFAULT: use built-in object hashCode()
        // hashCode() is built into every Java object
        // Math.abs handles negative hash codes, % capacity ensures it fits in the array
        return Math.abs(key.hashCode()) % capacity;
    }

}
