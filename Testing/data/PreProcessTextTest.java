package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.PreProcessText;

/**
 * JUnit 5 tests for PreProcessText (Data Layer).
 * Tests Arabic text preprocessing including diacritic removal and non-Arabic filtering.
 */
public class PreProcessTextTest {

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: preprocessText should return lowercase output")
    public void testPreprocessReturnsLowercase() {
        String result = PreProcessText.preprocessText("HELLO");
        assertEquals(result, result.toLowerCase(), "Output should be lowercase");
    }

    @Test
    @DisplayName("Positive: preprocessText should keep Arabic characters")
    public void testPreprocessKeepsArabic() {
        String input = "بسم الله الرحمن الرحيم";
        String result = PreProcessText.preprocessText(input);
        assertFalse(result.trim().isEmpty(), "Arabic text should be preserved");
    }

    @Test
    @DisplayName("Positive: removeNonArabicCharacters should strip English text")
    public void testRemoveNonArabicStripsEnglish() {
        String result = PreProcessText.removeNonArabicCharacters("Hello World");
        // Only the space between Hello and World is preserved, letters removed
        assertFalse(result.contains("H"), "English letters should be removed");
        assertFalse(result.contains("o"), "English letters should be removed");
        assertTrue(result.contains(" "), "Spaces should be preserved");
    }

    @Test
    @DisplayName("Positive: removeNonArabicCharacters should keep Arabic and spaces")
    public void testRemoveNonArabicKeepsArabic() {
        String input = "مرحبا بالعالم";
        String result = PreProcessText.removeNonArabicCharacters(input);
        assertEquals(input, result, "Arabic text and spaces should remain unchanged");
    }

    @Test
    @DisplayName("Positive: Mixed Arabic and English should keep only Arabic")
    public void testMixedArabicEnglish() {
        String input = "Hello مرحبا World بالعالم";
        String result = PreProcessText.removeNonArabicCharacters(input);
        assertTrue(result.contains("مرحبا"), "Arabic words should be preserved");
        assertTrue(result.contains("بالعالم"), "Arabic words should be preserved");
        assertFalse(result.contains("Hello"), "English should be removed");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: Empty string should return empty string")
    public void testPreprocessEmptyString() {
        String result = PreProcessText.preprocessText("");
        assertEquals("", result, "Empty input should return empty output");
    }

    @Test
    @DisplayName("Negative: Numbers should be removed by preprocessing")
    public void testPreprocessRemovesNumbers() {
        String result = PreProcessText.removeNonArabicCharacters("123 456");
        assertFalse(result.contains("1"), "Numbers should be removed");
        assertFalse(result.contains("4"), "Numbers should be removed");
    }

    @Test
    @DisplayName("Negative: Special characters should be removed")
    public void testPreprocessRemovesSpecialChars() {
        String result = PreProcessText.removeNonArabicCharacters("!@#$%^&*()");
        assertTrue(result.trim().isEmpty(), "Special characters should be removed");
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: Single Arabic character should be preserved")
    public void testSingleArabicChar() {
        String result = PreProcessText.removeNonArabicCharacters("م");
        assertEquals("م", result, "Single Arabic character should be preserved");
    }

    @Test
    @DisplayName("Boundary: Single space should be preserved")
    public void testSingleSpace() {
        String result = PreProcessText.removeNonArabicCharacters(" ");
        assertEquals(" ", result, "Space should be preserved");
    }

    @Test
    @DisplayName("Boundary: removeHarakat should not crash on empty string")
    public void testRemoveHarakatEmpty() {
        String result = PreProcessText.removeHarakat("");
        assertEquals("", result, "Empty input should return empty output");
    }
}


