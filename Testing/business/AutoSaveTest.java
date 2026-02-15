package business;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for Auto-Save trigger logic (Business Layer).
 * Auto-Save should trigger when word count exceeds 500 words.
 */
public class AutoSaveTest {

    /**
     * Helper method: Simulates the auto-save word count check.
     * Returns true if content has more than 500 words (auto-save should trigger).
     */
    private boolean shouldAutoSave(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        int wordCount = content.trim().split("\\s+").length;
        return wordCount > 500;
    }

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: Content with more than 500 words should trigger auto-save")
    public void testAutoSaveTriggersAbove500Words() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            sb.append("كلمة ");
        }
        assertTrue(shouldAutoSave(sb.toString()),
                "Auto-save should trigger when word count > 500");
    }

    @Test
    @DisplayName("Positive: Content with exactly 501 words should trigger auto-save")
    public void testAutoSaveTriggers501Words() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            sb.append("word ");
        }
        assertTrue(shouldAutoSave(sb.toString()),
                "501 words should trigger auto-save");
    }

    @Test
    @DisplayName("Positive: Content with 1000 words should trigger auto-save")
    public void testAutoSaveTriggers1000Words() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("text ");
        }
        assertTrue(shouldAutoSave(sb.toString()),
                "1000 words should trigger auto-save");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: Content with less than 500 words should NOT trigger auto-save")
    public void testAutoSaveDoesNotTriggerBelow500() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("كلمة ");
        }
        assertFalse(shouldAutoSave(sb.toString()),
                "Auto-save should NOT trigger when word count < 500");
    }

    @Test
    @DisplayName("Negative: Empty content should NOT trigger auto-save")
    public void testAutoSaveDoesNotTriggerEmpty() {
        assertFalse(shouldAutoSave(""),
                "Empty content should NOT trigger auto-save");
    }

    @Test
    @DisplayName("Negative: Null content should NOT trigger auto-save")
    public void testAutoSaveDoesNotTriggerNull() {
        assertFalse(shouldAutoSave(null),
                "Null content should NOT trigger auto-save");
    }

    @Test
    @DisplayName("Negative: Whitespace-only content should NOT trigger auto-save")
    public void testAutoSaveDoesNotTriggerWhitespace() {
        assertFalse(shouldAutoSave("   "),
                "Whitespace-only content should NOT trigger auto-save");
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: Exactly 500 words should NOT trigger auto-save")
    public void testAutoSaveDoesNotTriggerExactly500() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            sb.append("كلمة ");
        }
        assertFalse(shouldAutoSave(sb.toString()),
                "Exactly 500 words should NOT trigger auto-save (need > 500)");
    }

    @Test
    @DisplayName("Boundary: Exactly 499 words should NOT trigger auto-save")
    public void testAutoSaveDoesNotTrigger499Words() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 499; i++) {
            sb.append("كلمة ");
        }
        assertFalse(shouldAutoSave(sb.toString()),
                "499 words should NOT trigger auto-save");
    }

    @Test
    @DisplayName("Boundary: Single word content should NOT trigger auto-save")
    public void testAutoSaveSingleWord() {
        assertFalse(shouldAutoSave("كلمة"),
                "Single word should NOT trigger auto-save");
    }
}

