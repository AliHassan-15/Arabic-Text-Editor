package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.HashCalculator;

/**
 * JUnit 5 tests for HashCalculator (Data Layer).
 * Tests MD5 hashing integrity for file content.
 */
public class HashCalculatorTest {

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: Same input text should always produce the same MD5 hash")
    public void testSameInputProducesSameHash() throws Exception {
        String text = "Hello World";
        String hash1 = HashCalculator.calculateHash(text);
        String hash2 = HashCalculator.calculateHash(text);
        assertEquals(hash1, hash2, "Same input should always return the same hash");
    }

    @Test
    @DisplayName("Positive: Known MD5 hash verification for 'Hello World'")
    public void testKnownMD5Hash() throws Exception {
        // MD5 of "Hello World" is B10A8DB164E0754105B7A99BE72E3FE5
        String text = "Hello World";
        String expectedHash = "B10A8DB164E0754105B7A99BE72E3FE5";
        String actualHash = HashCalculator.calculateHash(text);
        assertEquals(expectedHash, actualHash, "Hash should match known MD5 value");
    }

    @Test
    @DisplayName("Positive: Hash should be a 32-character hex string")
    public void testHashLength() throws Exception {
        String text = "Test content for hashing";
        String hash = HashCalculator.calculateHash(text);
        assertEquals(32, hash.length(), "MD5 hash should be 32 characters long");
    }

    @Test
    @DisplayName("Positive: Arabic text should produce a valid hash")
    public void testArabicTextHash() throws Exception {
        String arabicText = "بسم الله الرحمن الرحيم";
        String hash = HashCalculator.calculateHash(arabicText);
        assertNotNull(hash, "Hash should not be null for Arabic text");
        assertEquals(32, hash.length(), "Hash length should be 32 for Arabic text");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: Different inputs should produce different hashes")
    public void testDifferentInputProducesDifferentHash() throws Exception {
        String text1 = "Hello World";
        String text2 = "Hello World!";
        String hash1 = HashCalculator.calculateHash(text1);
        String hash2 = HashCalculator.calculateHash(text2);
        assertNotEquals(hash1, hash2, "Different inputs should produce different hashes");
    }

    @Test
    @DisplayName("Negative: Editing content changes hash (integrity check)")
    public void testEditingContentChangesHash() throws Exception {
        String original = "Original document content";
        String edited = "Original document content - edited";
        String originalHash = HashCalculator.calculateHash(original);
        String editedHash = HashCalculator.calculateHash(edited);
        assertNotEquals(originalHash, editedHash,
                "Editing a file should change the hash value");
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: Empty string should produce a valid hash")
    public void testEmptyStringHash() throws Exception {
        String text = "";
        String hash = HashCalculator.calculateHash(text);
        assertNotNull(hash, "Empty string should still produce a hash");
        assertEquals(32, hash.length(), "Empty string hash should be 32 characters");
    }

    @Test
    @DisplayName("Boundary: Single character should produce a valid hash")
    public void testSingleCharacterHash() throws Exception {
        String text = "A";
        String hash = HashCalculator.calculateHash(text);
        assertNotNull(hash, "Single character should produce a hash");
        assertEquals(32, hash.length(), "Single char hash should be 32 characters");
    }

    @Test
    @DisplayName("Boundary: Very long string should produce a valid hash")
    public void testVeryLongStringHash() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("LongText ");
        }
        String hash = HashCalculator.calculateHash(sb.toString());
        assertNotNull(hash, "Long string should produce a hash");
        assertEquals(32, hash.length(), "Long string hash should be 32 characters");
    }
}

