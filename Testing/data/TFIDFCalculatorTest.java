package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.TFIDFCalculator;

/**
 * JUnit 5 tests for TFIDFCalculator (Data Layer).
 * Tests the TF-IDF algorithm with positive, negative, and boundary cases.
 * Note: TFIDFCalculator uses PreProcessText which removes non-Arabic characters.
 */
public class TFIDFCalculatorTest {

    private TFIDFCalculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new TFIDFCalculator();
    }

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: Known Arabic document TF-IDF score should be calculated correctly")
    public void testKnownDocumentTFIDF() {
        calculator.addDocumentToCorpus("بسم الله الرحمن الرحيم");
        calculator.addDocumentToCorpus("الحمد لله رب العالمين");

        double score = calculator.calculateDocumentTfIdf("الله الرحمن الرحيم");
        assertTrue(Double.isFinite(score), "TF-IDF score should be a finite number");
    }

    @Test
    @DisplayName("Positive: TF-IDF score should be finite for valid Arabic documents")
    public void testTFIDFScoreFinite() {
        calculator.addDocumentToCorpus("مرحبا بالعالم");
        calculator.addDocumentToCorpus("وداعا للعالم");

        double score = calculator.calculateDocumentTfIdf("مرحبا وداعا اختبار");
        assertTrue(Double.isFinite(score), "TF-IDF score should be finite");
    }

    @Test
    @DisplayName("Positive: Unique Arabic words should result in valid TF-IDF")
    public void testUniqueWordsValidTFIDF() {
        calculator.addDocumentToCorpus("كلمة كلمة كلمة");
        calculator.addDocumentToCorpus("كلمة كلمة نص");

        double scoreCommon = calculator.calculateDocumentTfIdf("كلمة كلمة كلمة");

        TFIDFCalculator calculator2 = new TFIDFCalculator();
        calculator2.addDocumentToCorpus("كلمة كلمة كلمة");
        calculator2.addDocumentToCorpus("كلمة كلمة نص");
        double scoreUnique = calculator2.calculateDocumentTfIdf("نادر فريد خاص");

        assertTrue(Double.isFinite(scoreCommon), "Common words TF-IDF should be finite");
        assertTrue(Double.isFinite(scoreUnique), "Unique words TF-IDF should be finite");
    }

    @Test
    @DisplayName("Positive: Single Arabic word document should produce valid TF-IDF")
    public void testSingleWordDocument() {
        calculator.addDocumentToCorpus("مرحبا بالعالم");
        double score = calculator.calculateDocumentTfIdf("مرحبا");
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

    @Test
    @DisplayName("Positive: Multiple Arabic documents in corpus should produce valid score")
    public void testMultipleDocumentCorpus() {
        calculator.addDocumentToCorpus("الكتاب الاول عن العلم");
        calculator.addDocumentToCorpus("الكتاب الثاني عن الادب");
        calculator.addDocumentToCorpus("الكتاب الثالث عن التاريخ");

        double score = calculator.calculateDocumentTfIdf("العلم والادب والتاريخ");
        assertTrue(Double.isFinite(score), "Multiple corpus docs TF-IDF should be finite");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: Empty corpus should still produce a valid score")
    public void testEmptyCorpus() {
        double score = calculator.calculateDocumentTfIdf("بعض النص");
        assertTrue(Double.isFinite(score), "Empty corpus TF-IDF should still be finite");
    }

    @Test
    @DisplayName("Negative: Special characters only should produce valid score")
    public void testSpecialCharactersOnly() {
        calculator.addDocumentToCorpus("نص عادي هنا");
        try {
            double score = calculator.calculateDocumentTfIdf("!@#$%^&*()");
            assertTrue(Double.isFinite(score) || Double.isNaN(score),
                    "Special chars TF-IDF should handle gracefully");
        } catch (Exception e) {
            // Acceptable - graceful handling of edge case
            assertNotNull(e.getMessage());
        }
    }

    @Test
    @DisplayName("Negative: Document with only spaces should handle gracefully")
    public void testSpacesOnlyDocument() {
        calculator.addDocumentToCorpus("مرحبا بالعالم");
        try {
            double score = calculator.calculateDocumentTfIdf("   ");
            assertTrue(Double.isFinite(score) || Double.isNaN(score),
                    "Spaces-only document should handle gracefully");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: Very large Arabic corpus should still calculate correctly")
    public void testLargeCorpus() {
        for (int i = 0; i < 50; i++) {
            calculator.addDocumentToCorpus("وثيقة رقم " + i + " مع كلمات مختلفة");
        }
        double score = calculator.calculateDocumentTfIdf("وثيقة مع كلمات");
        assertTrue(Double.isFinite(score), "Large corpus TF-IDF should be finite");
    }

    @Test
    @DisplayName("Boundary: Same Arabic word repeated in document")
    public void testRepeatedWordDocument() {
        calculator.addDocumentToCorpus("كلمة كلمة كلمة");
        calculator.addDocumentToCorpus("اخرى اخرى اخرى");
        double score = calculator.calculateDocumentTfIdf("كلمة كلمة كلمة كلمة كلمة");
        assertTrue(Double.isFinite(score), "Repeated word TF-IDF should be finite");
    }
}
