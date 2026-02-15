package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.PaginationDAO;
import dto.Pages;

/**
 * JUnit 5 tests for PaginationDAO (Data Layer).
 * Tests the pagination logic that splits content into pages of 100 characters.
 */
public class PaginationDAOTest {

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: Content shorter than page size should produce 1 page")
    public void testShortContentSinglePage() {
        String content = "Short text";
        List<Pages> pages = PaginationDAO.paginate(content);
        assertEquals(1, pages.size(), "Short content should produce exactly 1 page");
        assertEquals(content, pages.get(0).getPageContent(), "Page content should match input");
        assertEquals(1, pages.get(0).getPageNumber(), "Page number should be 1");
    }

    @Test
    @DisplayName("Positive: Content exactly 100 chars should produce 1 page")
    public void testExactPageSizeContent() {
        // Create a string of exactly 100 characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("A");
        }
        String content = sb.toString();
        assertEquals(100, content.length());

        List<Pages> pages = PaginationDAO.paginate(content);
        assertEquals(1, pages.size(), "Exactly 100 chars should produce 1 page");
        assertEquals(content, pages.get(0).getPageContent());
    }

    @Test
    @DisplayName("Positive: Content of 101 chars should produce 2 pages")
    public void testContentProducesTwoPages() {
        // Create a string of 101 characters
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            sb.append("B");
        }
        String content = sb.toString();

        List<Pages> pages = PaginationDAO.paginate(content);
        assertEquals(2, pages.size(), "101 chars should produce 2 pages");
        assertEquals(100, pages.get(0).getPageContent().length(), "First page should have 100 chars");
        assertEquals(1, pages.get(1).getPageContent().length(), "Second page should have 1 char");
    }

    @Test
    @DisplayName("Positive: Content of 250 chars should produce 3 pages")
    public void testMultiplePages() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 250; i++) {
            sb.append("C");
        }
        String content = sb.toString();

        List<Pages> pages = PaginationDAO.paginate(content);
        assertEquals(3, pages.size(), "250 chars should produce 3 pages");
        assertEquals(100, pages.get(0).getPageContent().length(), "Page 1 should have 100 chars");
        assertEquals(100, pages.get(1).getPageContent().length(), "Page 2 should have 100 chars");
        assertEquals(50, pages.get(2).getPageContent().length(), "Page 3 should have 50 chars");
    }

    @Test
    @DisplayName("Positive: Page numbers should be sequential starting from 1")
    public void testPageNumbersAreSequential() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 350; i++) {
            sb.append("D");
        }

        List<Pages> pages = PaginationDAO.paginate(sb.toString());
        for (int i = 0; i < pages.size(); i++) {
            assertEquals(i + 1, pages.get(i).getPageNumber(),
                    "Page number should be " + (i + 1));
        }
    }

    @Test
    @DisplayName("Positive: Arabic content should paginate correctly")
    public void testArabicContentPagination() {
        StringBuilder sb = new StringBuilder();
        // Build Arabic content longer than 100 chars
        for (int i = 0; i < 30; i++) {
            sb.append("بسم الله ");
        }
        String content = sb.toString();
        assertTrue(content.length() > 100, "Content should be longer than 100 chars");

        List<Pages> pages = PaginationDAO.paginate(content);
        assertTrue(pages.size() > 1, "Arabic content > 100 chars should produce multiple pages");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: Null content should return a list with one empty page")
    public void testNullContent() {
        List<Pages> pages = PaginationDAO.paginate(null);
        assertNotNull(pages, "Result should not be null");
        assertEquals(1, pages.size(), "Null content should produce 1 empty page");
        assertEquals("", pages.get(0).getPageContent(), "Page content should be empty string");
    }

    @Test
    @DisplayName("Negative: Empty string should return a list with one empty page")
    public void testEmptyContent() {
        List<Pages> pages = PaginationDAO.paginate("");
        assertNotNull(pages, "Result should not be null");
        assertEquals(1, pages.size(), "Empty content should produce 1 empty page");
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: Content of exactly 1 character")
    public void testSingleCharContent() {
        List<Pages> pages = PaginationDAO.paginate("X");
        assertEquals(1, pages.size(), "Single char should produce 1 page");
        assertEquals("X", pages.get(0).getPageContent());
    }

    @Test
    @DisplayName("Boundary: Content of exactly 99 chars (one less than page size)")
    public void testOneLessThanPageSize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 99; i++) {
            sb.append("E");
        }
        List<Pages> pages = PaginationDAO.paginate(sb.toString());
        assertEquals(1, pages.size(), "99 chars should produce 1 page");
    }

    @Test
    @DisplayName("Boundary: Content of exactly 200 chars (two full pages)")
    public void testTwoFullPages() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append("F");
        }
        List<Pages> pages = PaginationDAO.paginate(sb.toString());
        assertEquals(2, pages.size(), "200 chars should produce exactly 2 pages");
        assertEquals(100, pages.get(0).getPageContent().length());
        assertEquals(100, pages.get(1).getPageContent().length());
    }
}

