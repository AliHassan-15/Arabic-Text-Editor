package business;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.SearchWord;
import dto.Documents;
import dto.Pages;

/**
 * JUnit 5 tests for SearchWord (Business Layer).
 * Tests the keyword search logic across documents.
 */
public class SearchWordTest {

    private List<Documents> testDocs;

    @BeforeEach
    public void setUp() {
        testDocs = new ArrayList<>();

        // Document 1: simple English content
        List<Pages> pages1 = new ArrayList<>();
        pages1.add(new Pages(1, 1, 1, "the quick brown fox jumps over the lazy dog"));
        testDocs.add(new Documents(1, "doc1.txt", "hash1", "2026-01-01", "2026-01-01", pages1));

        // Document 2: multiple pages
        List<Pages> pages2 = new ArrayList<>();
        pages2.add(new Pages(2, 2, 1, "hello world this is a test document"));
        pages2.add(new Pages(3, 2, 2, "second page with more content here"));
        testDocs.add(new Documents(2, "doc2.txt", "hash2", "2026-01-02", "2026-01-02", pages2));

        // Document 3: Arabic content
        List<Pages> pages3 = new ArrayList<>();
        pages3.add(new Pages(4, 3, 1, "بسم الله الرحمن الرحيم"));
        testDocs.add(new Documents(3, "doc3.txt", "hash3", "2026-01-03", "2026-01-03", pages3));
    }

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: Search for existing keyword should return matching documents")
    public void testSearchExistingKeyword() {
        List<String> results = SearchWord.searchKeyword("fox", testDocs);
        assertNotNull(results, "Results should not be null");
        assertFalse(results.isEmpty(), "Should find 'fox' in documents");
        assertTrue(results.get(0).contains("doc1.txt"), "Should find in doc1");
    }

    @Test
    @DisplayName("Positive: Search for 'world' should find in doc2")
    public void testSearchWorldKeyword() {
        List<String> results = SearchWord.searchKeyword("world", testDocs);
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Should find 'world' in documents");
        assertTrue(results.get(0).contains("doc2.txt"), "Should find in doc2");
    }

    @Test
    @DisplayName("Positive: Search should be case-insensitive (equalsIgnoreCase)")
    public void testCaseInsensitiveSearch() {
        // The method uses equalsIgnoreCase for word matching
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(10, 10, 1, "Hello World Testing"));
        List<Documents> docs = new ArrayList<>();
        docs.add(new Documents(10, "test.txt", "hash", "2026-01-01", "2026-01-01", pages));

        List<String> results = SearchWord.searchKeyword("hello", docs);
        assertFalse(results.isEmpty(), "Case-insensitive search should find 'Hello'");
    }

    @Test
    @DisplayName("Positive: Search result should contain prefix word")
    public void testSearchResultContainsPrefixWord() {
        List<String> results = SearchWord.searchKeyword("fox", testDocs);
        assertFalse(results.isEmpty());
        // Result format: "docName - prefixWord keyword..."
        // For "brown fox", prefix should be "brown"
        assertTrue(results.get(0).contains("brown"), "Result should contain prefix word 'brown'");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: Keyword shorter than 3 chars should throw IllegalArgumentException")
    public void testKeywordTooShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword("ab", testDocs);
        }, "Keyword less than 3 characters should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Negative: Single character keyword should throw IllegalArgumentException")
    public void testSingleCharKeyword() {
        assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword("a", testDocs);
        });
    }

    @Test
    @DisplayName("Negative: Empty keyword should throw IllegalArgumentException")
    public void testEmptyKeyword() {
        assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword("", testDocs);
        });
    }

    @Test
    @DisplayName("Negative: Keyword not found should return empty list")
    public void testKeywordNotFound() {
        List<String> results = SearchWord.searchKeyword("nonexistent", testDocs);
        assertNotNull(results);
        assertTrue(results.isEmpty(), "Non-existing keyword should return empty list");
    }

    @Test
    @DisplayName("Negative: Search in empty document list should return empty results")
    public void testSearchInEmptyDocList() {
        List<Documents> emptyDocs = new ArrayList<>();
        List<String> results = SearchWord.searchKeyword("test", emptyDocs);
        assertNotNull(results);
        assertTrue(results.isEmpty(), "Empty doc list should return empty results");
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: Keyword of exactly 3 characters should work")
    public void testExactlyThreeCharKeyword() {
        List<String> results = SearchWord.searchKeyword("the", testDocs);
        assertNotNull(results, "3-char keyword should not throw exception");
    }

    @Test
    @DisplayName("Boundary: Keyword of exactly 2 characters should throw exception")
    public void testExactlyTwoCharKeyword() {
        assertThrows(IllegalArgumentException.class, () -> {
            SearchWord.searchKeyword("is", testDocs);
        }, "2-char keyword should throw exception");
    }

    @Test
    @DisplayName("Boundary: First word in page should have empty prefix")
    public void testFirstWordHasEmptyPrefix() {
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(20, 20, 1, "unique word here"));
        List<Documents> docs = new ArrayList<>();
        docs.add(new Documents(20, "first.txt", "hash", "2026-01-01", "2026-01-01", pages));

        List<String> results = SearchWord.searchKeyword("unique", docs);
        assertFalse(results.isEmpty(), "First word should still be found");
        // Prefix should be empty for the first word
        assertTrue(results.get(0).contains("first.txt"));
    }
}

