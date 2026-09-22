public class SimpleMapHashTable<K, V> implements SimpleMapHashTableInterface<K, V> {

    // Definition of a hash table entry (nested class)
    static class HashEntry<K, V> {
        K key;
        V value;
        // Below is pointer to next entry (linked list)
        HashEntry<K, V> next;

        // Constructor for a single entry
        public HashEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Definition of storage (use array of buckets here)
    // Buckets are actually linked lists
    HashEntry<K, V>[] arrayOfEntries;
    int capacity;
    // add hashFunction member
    String hashFunction = "Default";

    // The Hash Function call
    private int hashFunctionGetIndex(K key) {
        return SimpleMapHashTableFunctions.runHashFunction(this.hashFunction, key, this.capacity);
    }

    @SuppressWarnings("unchecked")
    public SimpleMapHashTable(int capacity) {
        this.capacity = capacity;
        // Create a raw array and cast it for generics
        this.arrayOfEntries = (HashEntry<K, V>[]) new HashEntry[capacity];
    }

    public SimpleMapHashTable(int capacity, String hashFunction) {
        this.capacity = capacity;
        // Set the hashFunction name associated with the class object
        this.hashFunction = hashFunction;
        // Create a raw array and cast it for generics
        this.arrayOfEntries = (HashEntry<K, V>[]) new HashEntry[capacity];
    }

    // The Put Method (Insert or Update)
    @Override
    public void put(K key, V value) {
        int index = hashFunctionGetIndex(key);
        HashEntry<K, V> head = arrayOfEntries[index];

        // Check if the key already exists in the bucket
        while (head != null) {
            if ((head.key == null && key == null) || (head.key != null && head.key.equals(key))) {
                head.value = value; // Update the value
                return;
            }
            head = head.next;
        }

        // Key not found: Insert new node at the "head" of the bucket list
        HashEntry<K, V> newNode = new HashEntry<>(key, value);
        newNode.next = arrayOfEntries[index];
        arrayOfEntries[index] = newNode;
    }

    // The Get Method (Retrieval)
    @Override
    public V get(K key) {
        int index = hashFunctionGetIndex(key);
        HashEntry<K, V> head = arrayOfEntries[index];
        // Walk through the linked list in the bucket
        while (head != null) {
            if ((head.key == null && key == null) || (head.key != null && head.key.equals(key))) {
                return head.value;
            }
            head = head.next;
        }
        return null; // Key not found
    }

    // Supporting methods below

    public int getCapacity() {
        return capacity;
    }

    public HashEntry<K, V>[] getBuckets() {
        return arrayOfEntries;
    }

    // Get a single string of the table contents for print
    @Override
    public String getTableContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleMapHashTable Contents (Capacity: ").append(capacity).append("):\n");
        for (int i = 0; i < capacity; i++) {
            sb.append("Bucket ").append(i).append(": ");
            HashEntry<K, V> current = arrayOfEntries[i];
            if (current == null) {
                sb.append("null");
            } else {
                while (current != null) {
                    sb.append("[").append(current.key).append(" : ").append(current.value).append("]");
                    if (current.next != null) {
                        sb.append(" -> ");
                    }
                    current = current.next;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    //@Override
    public String getTableKeys() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleMapHashTable Contents (Capacity: ").append(capacity).append("):\n");
        sb.append("Hash Function: ").append(hashFunction).append("\n");
        int numinbucket;
        for (int i = 0; i < capacity; i++) {
            numinbucket = 0;
            sb.append("Bucket ").append(i).append(": ");
            HashEntry<K, V> current = arrayOfEntries[i];
            if (current == null) {
                sb.append("null");
            } else {
                StringBuilder bucketKeys = new StringBuilder();
                while (current != null) {
                    numinbucket++;
                    bucketKeys.append(current.key);
                    if (current.next != null) {
                        bucketKeys.append(", ");
                    }
                    current = current.next; // Always advance to the next node
                }
                sb.append("NumInBucket: ").append(numinbucket).append(" | Keys: ").append(bucketKeys.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getTableCounts() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleMapHashTable Contents (Capacity: ").append(capacity).append("):\n");
        sb.append("Hash Function: ").append(hashFunction).append("\n");
        int numinbucket;
        int totalitems = 0;
        for (int i = 0; i < capacity; i++) {
            numinbucket = 0;
            sb.append("Bucket ").append(i).append(": ");
            HashEntry<K, V> current = arrayOfEntries[i];
            while (current != null) {
                numinbucket++;
                current = current.next;
            }
            sb.append("NumInBucket: " + numinbucket + "\n");
            totalitems += numinbucket;
        }
        sb.append("Total Items: " + totalitems + "\n");
        return sb.toString();
    }
}
