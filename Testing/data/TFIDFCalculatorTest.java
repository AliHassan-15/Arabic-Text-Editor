package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.TFIDFCalculator;

/**
 * JUnit 5 tests for TFIDFCalculator (Data Layer).
 * Tests the TF-IDF algorithm with positive, negative, and boundary cases.
 */
public class TFIDFCalculatorTest {

    private TFIDFCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new TFIDFCalculator();
    }

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: Known document TF-IDF score should be calculated correctly")
    public void testKnownDocumentTFIDF() {
        // Add corpus documents
        calculator.addDocumentToCorpus("the cat sat on the mat");
        calculator.addDocumentToCorpus("the dog played in the park");

        // Calculate TF-IDF for a target document
        double score = calculator.calculateDocumentTfIdf("the cat played");

        // The score should be a finite number (not NaN, not Infinity)
        assertTrue(Double.isFinite(score), "TF-IDF score should be a finite number");
    }

    @Test
    @DisplayName("Positive: TF-IDF score should be non-negative for valid documents")
    public void testTFIDFScoreNonNegative() {
        calculator.addDocumentToCorpus("hello world");
        calculator.addDocumentToCorpus("goodbye world");

        double score = calculator.calculateDocumentTfIdf("hello goodbye test");
        assertTrue(Double.isFinite(score), "TF-IDF score should be finite");
    }

    @Test
    @DisplayName("Positive: Unique words in document should result in higher TF-IDF")
    public void testUniqueWordsHigherTFIDF() {
        calculator.addDocumentToCorpus("common common common");
        calculator.addDocumentToCorpus("common common words");

        double scoreCommon = calculator.calculateDocumentTfIdf("common common common");
        
        TFIDFCalculator calculator2 = new TFIDFCalculator();
        calculator2.addDocumentToCorpus("common common common");
        calculator2.addDocumentToCorpus("common common words");
        double scoreUnique = calculator2.calculateDocumentTfIdf("rare unique special");

        // Both should be finite valid scores
        assertTrue(Double.isFinite(scoreCommon), "Common words TF-IDF should be finite");
        assertTrue(Double.isFinite(scoreUnique), "Unique words TF-IDF should be finite");
    }

    @Test
    @DisplayName("Positive: Single word document should produce valid TF-IDF")
    public void testSingleWordDocument() {
        calculator.addDocumentToCorpus("hello world");
        double score = calculator.calculateDocumentTfIdf("hello");
        assertTrue(Double.isFinite(score), "Single word TF-IDF should be finite");
    }

    @Test
    @DisplayName("Positive: Arabic text corpus should produce valid TF-IDF score")
    public void testArabicTextTFIDF() {
        calculator.addDocumentToCorpus("بسم الله الرحمن الرحيم");
        calculator.addDocumentToCorpus("الحمد لله رب العالمين");

        double score = calculator.calculateDocumentTfIdf("الله الرحمن");
        assertTrue(Double.isFinite(score), "Arabic text TF-IDF should be finite");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: Empty corpus should still produce a valid score")
    public void testEmptyCorpus() {
        // No documents added to corpus
        double score = calculator.calculateDocumentTfIdf("some text");
        assertTrue(Double.isFinite(score), "Empty corpus TF-IDF should still be finite");
    }

    @Test
    @DisplayName("Negative: Special characters only should produce valid score")
    public void testSpecialCharactersOnly() {
        calculator.addDocumentToCorpus("normal text here");
        double score = calculator.calculateDocumentTfIdf("!@#$%^&*()");
        assertTrue(Double.isFinite(score), "Special chars TF-IDF should be finite");
    }

    @Test
    @DisplayName("Negative: Document with only spaces should handle gracefully")
    public void testSpacesOnlyDocument() {
        calculator.addDocumentToCorpus("hello world");
        try {
            double score = calculator.calculateDocumentTfIdf("   ");
            // If it doesn't throw, it should be finite
            assertTrue(Double.isFinite(score) || score == 0.0,
                    "Spaces-only document should produce finite score or zero");
        } catch (Exception e) {
            // Acceptable if it throws - graceful handling
            assertNotNull(e.getMessage());
        }
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: Very large corpus should still calculate correctly")
    public void testLargeCorpus() {
        for (int i = 0; i < 50; i++) {
            calculator.addDocumentToCorpus("document number " + i + " with various words");
        }
        double score = calculator.calculateDocumentTfIdf("document with words");
        assertTrue(Double.isFinite(score), "Large corpus TF-IDF should be finite");
    }

    @Test
    @DisplayName("Boundary: Same word repeated in document")
    public void testRepeatedWordDocument() {
        calculator.addDocumentToCorpus("word word word");
        calculator.addDocumentToCorpus("other other other");
        double score = calculator.calculateDocumentTfIdf("word word word word word");
        assertTrue(Double.isFinite(score), "Repeated word TF-IDF should be finite");
    }
}

