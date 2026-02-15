package presentation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for word count, line count, and average word length
 * utility logic used in the Presentation Layer (EditorPO).
 */
public class WordCountTest {

    // ---- Helper methods matching EditorPO logic ----

    private int calculateWordCount(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    private int calculateLineCount(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        String[] lines = content.split("\r?\n");
        return lines.length;
    }

    private double calculateAvgWordLength(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }
        String[] words = content.split("\\s+");
        int totalLength = 0;
        int wordCount = 0;
        for (String word : words) {
            if (!word.isEmpty()) {
                totalLength += word.length();
                wordCount++;
            }
        }
        return wordCount == 0 ? 0 : (double) totalLength / wordCount;
    }

    // ==================== Word Count Tests ====================

    @Test
    @DisplayName("Positive: Word count of normal sentence")
    public void testWordCountNormal() {
        assertEquals(5, calculateWordCount("the quick brown fox jumps"));
    }

    @Test
    @DisplayName("Positive: Word count of Arabic text")
    public void testWordCountArabic() {
        assertEquals(4, calculateWordCount("بسم الله الرحمن الرحيم"));
    }

    @Test
    @DisplayName("Negative: Word count of empty string should be 0")
    public void testWordCountEmpty() {
        assertEquals(0, calculateWordCount(""));
    }

    @Test
    @DisplayName("Negative: Word count of null should be 0")
    public void testWordCountNull() {
        assertEquals(0, calculateWordCount(null));
    }

    @Test
    @DisplayName("Boundary: Word count of single word")
    public void testWordCountSingle() {
        assertEquals(1, calculateWordCount("hello"));
    }

    @Test
    @DisplayName("Boundary: Word count with extra spaces")
    public void testWordCountExtraSpaces() {
        assertEquals(3, calculateWordCount("  hello   world   test  "));
    }

    // ==================== Line Count Tests ====================

    @Test
    @DisplayName("Positive: Line count of multiline text")
    public void testLineCountMultiple() {
        assertEquals(3, calculateLineCount("line1\nline2\nline3"));
    }

    @Test
    @DisplayName("Positive: Line count of single line")
    public void testLineCountSingle() {
        assertEquals(1, calculateLineCount("single line"));
    }

    @Test
    @DisplayName("Negative: Line count of empty string should be 0")
    public void testLineCountEmpty() {
        assertEquals(0, calculateLineCount(""));
    }

    @Test
    @DisplayName("Negative: Line count of null should be 0")
    public void testLineCountNull() {
        assertEquals(0, calculateLineCount(null));
    }

    @Test
    @DisplayName("Boundary: Line count with Windows line endings")
    public void testLineCountWindows() {
        assertEquals(3, calculateLineCount("line1\r\nline2\r\nline3"));
    }

    // ==================== Avg Word Length Tests ====================

    @Test
    @DisplayName("Positive: Avg word length of known text")
    public void testAvgWordLengthKnown() {
        // "hi by" -> lengths 2, 2 -> avg = 2.0
        assertEquals(2.0, calculateAvgWordLength("hi by"), 0.01);
    }

    @Test
    @DisplayName("Negative: Avg word length of empty string should be 0")
    public void testAvgWordLengthEmpty() {
        assertEquals(0, calculateAvgWordLength(""));
    }

    @Test
    @DisplayName("Negative: Avg word length of null should be 0")
    public void testAvgWordLengthNull() {
        assertEquals(0, calculateAvgWordLength(null));
    }

    @Test
    @DisplayName("Boundary: Avg word length of single character word")
    public void testAvgWordLengthSingleChar() {
        assertEquals(1.0, calculateAvgWordLength("a"), 0.01);
    }
}

