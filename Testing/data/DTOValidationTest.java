package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dto.Documents;
import dto.Pages;

/**
 * JUnit 5 tests for DTO classes (Data Layer).
 * Validates Documents and Pages data transfer objects.
 */
public class DTOValidationTest {

    // ==================== Pages DTO Tests ====================

    @Test
    @DisplayName("Positive: Pages constructor should set all fields correctly")
    public void testPagesConstructor() {
        Pages page = new Pages(1, 10, 1, "Hello World");
        assertEquals(1, page.getPageId());
        assertEquals(10, page.getFileId());
        assertEquals(1, page.getPageNumber());
        assertEquals("Hello World", page.getPageContent());
    }

    @Test
    @DisplayName("Positive: Pages setters should update fields")
    public void testPagesSetters() {
        Pages page = new Pages(0, 0, 0, "");
        page.setPageId(5);
        page.setFileId(20);
        page.setPageNumber(3);
        page.setPageContent("Updated content");
        assertEquals(5, page.getPageId());
        assertEquals(20, page.getFileId());
        assertEquals(3, page.getPageNumber());
        assertEquals("Updated content", page.getPageContent());
    }

    @Test
    @DisplayName("Negative: Pages with empty content should store empty string")
    public void testPagesEmptyContent() {
        Pages page = new Pages(1, 1, 1, "");
        assertEquals("", page.getPageContent());
    }

    @Test
    @DisplayName("Boundary: Pages with zero IDs should be valid")
    public void testPagesZeroIds() {
        Pages page = new Pages(0, 0, 0, "content");
        assertEquals(0, page.getPageId());
        assertEquals(0, page.getFileId());
    }

    // ==================== Documents DTO Tests ====================

    @Test
    @DisplayName("Positive: Documents constructor should set all fields correctly")
    public void testDocumentsConstructor() {
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(1, 1, 1, "Page 1"));
        Documents doc = new Documents(1, "test.txt", "hash123", "2026-01-01", "2026-01-01", pages);
        assertEquals(1, doc.getId());
        assertEquals("test.txt", doc.getName());
        assertEquals("hash123", doc.getHash());
        assertEquals("2026-01-01", doc.getLastModified());
        assertEquals("2026-01-01", doc.getDateCreated());
        assertEquals(1, doc.getPages().size());
    }

    @Test
    @DisplayName("Positive: Documents setters should update fields")
    public void testDocumentsSetters() {
        Documents doc = new Documents(0, "", "", "", "", new ArrayList<>());
        doc.setId(10);
        doc.setName("updated.md");
        doc.setHash("newhash");
        doc.setLastModified("2026-02-15");
        doc.setDateCreated("2026-01-01");
        assertEquals(10, doc.getId());
        assertEquals("updated.md", doc.getName());
        assertEquals("newhash", doc.getHash());
        assertEquals("2026-02-15", doc.getLastModified());
    }

    @Test
    @DisplayName("Positive: Documents with multiple pages should store all pages")
    public void testDocumentsMultiplePages() {
        List<Pages> pages = new ArrayList<>();
        pages.add(new Pages(1, 1, 1, "Page 1 content"));
        pages.add(new Pages(2, 1, 2, "Page 2 content"));
        pages.add(new Pages(3, 1, 3, "Page 3 content"));
        Documents doc = new Documents(1, "multi.txt", "hash", "2026-01-01", "2026-01-01", pages);
        assertEquals(3, doc.getPages().size());
        assertEquals("Page 2 content", doc.getPages().get(1).getPageContent());
    }

    @Test
    @DisplayName("Negative: Documents with empty pages list should have 0 pages")
    public void testDocumentsEmptyPages() {
        Documents doc = new Documents(1, "empty.txt", "hash", "2026-01-01", "2026-01-01", new ArrayList<>());
        assertNotNull(doc.getPages());
        assertEquals(0, doc.getPages().size());
    }

    @Test
    @DisplayName("Boundary: Documents with Arabic name should store correctly")
    public void testDocumentsArabicName() {
        Documents doc = new Documents(1, "ملف عربي.txt", "hash", "2026-01-01", "2026-01-01", new ArrayList<>());
        assertEquals("ملف عربي.txt", doc.getName());
    }

    @Test
    @DisplayName("Positive: setPages should replace page list")
    public void testDocumentsSetPages() {
        Documents doc = new Documents(1, "test.txt", "hash", "2026-01-01", "2026-01-01", new ArrayList<>());
        List<Pages> newPages = new ArrayList<>();
        newPages.add(new Pages(10, 1, 1, "New page content"));
        doc.setPages(newPages);
        assertEquals(1, doc.getPages().size());
        assertEquals("New page content", doc.getPages().get(0).getPageContent());
    }
}

