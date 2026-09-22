import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleMapHashTableFunctionsTest {

    @Test
    @DisplayName("Test StringLength hash function with standard inputs")
    public void testStringLengthStandardInputs() {
        int capacity = 10;

        // "hello" length is 5 -> 5 % 10 = 5
        int hash1 = SimpleMapHashTableFunctions.runHashFunction("StringLength", "hello", capacity);
        assertEquals(5, hash1, "Hash of 'hello' with capacity 10 should be 5");

        // "a" length is 1 -> 1 % 10 = 1
        int hash2 = SimpleMapHashTableFunctions.runHashFunction("StringLength", "a", capacity);
        assertEquals(1, hash2, "Hash of 'a' with capacity 10 should be 1");

        // "" length is 0 -> 0 % 10 = 0
        int hash3 = SimpleMapHashTableFunctions.runHashFunction("StringLength", "", capacity);
        assertEquals(0, hash3, "Hash of empty string with capacity 10 should be 0");
    }

    @Test
    @DisplayName("Test StringLength hash function when length exceeds capacity")
    public void testStringLengthExceedingCapacity() {
        int capacity = 7;

        // "abcdefghijk" length is 11 -> 11 % 7 = 4
        int hash = SimpleMapHashTableFunctions.runHashFunction("StringLength", "abcdefghijk", capacity);
        assertEquals(4, hash, "Hash of length 11 string with capacity 7 should be 4 (11 % 7)");
    }

    @Test
    @DisplayName("Test CyclicShiftExample hash function determinism and boundary bounds")
    public void testCyclicShiftExampleBasic() {
        int capacity = 13;

        int hash1 = SimpleMapHashTableFunctions.runHashFunction("CyclicShiftExample", "test", capacity);
        int hash2 = SimpleMapHashTableFunctions.runHashFunction("CyclicShiftExample", "test", capacity);

        // Determinism: Same input produces same output
        assertEquals(hash1, hash2, "CyclicShift hash should be deterministic for identical input");

        // Result bounds: must be in range [0, capacity - 1]
        assertTrue(hash1 >= 0 && hash1 < capacity, "Hash code must be within range [0, capacity - 1]");
    }

    @Test
    @DisplayName("Test CyclicShiftExample hash function produces expected values")
    public void testCyclicShiftExampleExpectedValues() {
        int capacity = 10;

        // Verification of manual calculation for 'a' (ASCII 97):
        // h = (97 << 5) | (97 >>> 27) = 3104
        // Math.abs(3104) % 10 = 4
        int hashA = SimpleMapHashTableFunctions.runHashFunction("CyclicShiftExample", "a", capacity);
        assertEquals(4, hashA, "Hash for 'a' with capacity 10 should be 4");

        // Verification for empty string: h = 0 -> Math.abs(0) % 10 = 0
        int hashEmpty = SimpleMapHashTableFunctions.runHashFunction("CyclicShiftExample", "", capacity);
        assertEquals(0, hashEmpty, "Hash for empty string should be 0");
    }

    @Test
    @DisplayName("Test CyclicShiftExample sensitivity to character ordering")
    public void testCyclicShiftExampleOrderSensitivity() {
        int capacity = 1000;

        int hashAB = SimpleMapHashTableFunctions.runHashFunction("CyclicShiftExample", "ab", capacity);
        int hashBA = SimpleMapHashTableFunctions.runHashFunction("CyclicShiftExample", "ba", capacity);

        assertNotEquals(hashAB, hashBA, "'ab' and 'ba' should produce different hash codes due to cyclic shift");
    }
}
