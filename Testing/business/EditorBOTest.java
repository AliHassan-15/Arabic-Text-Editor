package business;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.EditorBO;
import bll.IEditorBO;
import dal.FacadeDAO;
import dal.IEditorDBDAO;
import dal.IFacadeDAO;
import dal.AbstractDAOEditorFactory;

/**
 * JUnit 5 tests for EditorBO (Business Layer).
 * Tests are designed against the IEditorBO interface for swappability.
 */
public class EditorBOTest {

    private IEditorBO editorBO;

    @BeforeEach
    public void setUp() {
        IEditorDBDAO editorDAO = AbstractDAOEditorFactory.getInstance().createEditorDAO();
        IFacadeDAO facadeDAO = new FacadeDAO(editorDAO);
        editorBO = new EditorBO(facadeDAO);
    }

    // ==================== getFileExtension() Positive Tests ====================

    @Test
    @DisplayName("Positive: getFileExtension for .txt file should return 'txt'")
    public void testGetFileExtensionTxt() {
        String extension = editorBO.getFileExtension("document.txt");
        assertEquals("txt", extension, "Extension of 'document.txt' should be 'txt'");
    }

    @Test
    @DisplayName("Positive: getFileExtension for .md file should return 'md'")
    public void testGetFileExtensionMd() {
        String extension = editorBO.getFileExtension("readme.md");
        assertEquals("md", extension, "Extension of 'readme.md' should be 'md'");
    }

    @Test
    @DisplayName("Positive: getFileExtension for .java file should return 'java'")
    public void testGetFileExtensionJava() {
        String extension = editorBO.getFileExtension("Main.java");
        assertEquals("java", extension);
    }

    @Test
    @DisplayName("Positive: getFileExtension with multiple dots should return last extension")
    public void testGetFileExtensionMultipleDots() {
        String extension = editorBO.getFileExtension("archive.backup.tar.gz");
        assertEquals("gz", extension, "Should return the last extension after the last dot");
    }

    // ==================== getFileExtension() Negative Tests ====================

    @Test
    @DisplayName("Negative: getFileExtension with no extension should return empty string")
    public void testGetFileExtensionNoExtension() {
        String extension = editorBO.getFileExtension("filename");
        assertEquals("", extension, "File without extension should return empty string");
    }

    @Test
    @DisplayName("Negative: getFileExtension with empty filename should return empty string")
    public void testGetFileExtensionEmptyFilename() {
        String extension = editorBO.getFileExtension("");
        assertEquals("", extension, "Empty filename should return empty string");
    }

    // ==================== getFileExtension() Boundary Tests ====================

    @Test
    @DisplayName("Boundary: getFileExtension with dot only should return empty string")
    public void testGetFileExtensionDotOnly() {
        String extension = editorBO.getFileExtension(".");
        assertEquals("", extension, "Dot-only filename should return empty after the dot");
    }

    @Test
    @DisplayName("Boundary: getFileExtension with dot at start (hidden file)")
    public void testGetFileExtensionHiddenFile() {
        String extension = editorBO.getFileExtension(".gitignore");
        assertEquals("gitignore", extension);
    }

    // ==================== getAllFiles() Tests ====================

    @Test
    @DisplayName("Positive: getAllFiles should return a non-null list")
    public void testGetAllFilesNotNull() {
        List<?> files = editorBO.getAllFiles();
        assertNotNull(files, "getAllFiles should never return null");
    }

    // ==================== getFile() Tests ====================

    @Test
    @DisplayName("Negative: getFile with non-existent ID should return null")
    public void testGetFileNonExistentId() {
        assertNull(editorBO.getFile(-999), "Non-existent file ID should return null");
    }

    @Test
    @DisplayName("Negative: getFile with zero ID should return null")
    public void testGetFileZeroId() {
        assertNull(editorBO.getFile(0), "File ID 0 should return null");
    }

    // ==================== searchKeyword() Tests ====================

    @Test
    @DisplayName("Negative: searchKeyword with short keyword should throw exception")
    public void testSearchKeywordTooShort() {
        assertThrows(IllegalArgumentException.class, () -> {
            editorBO.searchKeyword("ab");
        }, "Keyword less than 3 chars should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Positive: searchKeyword with valid keyword should return non-null list")
    public void testSearchKeywordValid() {
        List<String> results = editorBO.searchKeyword("test");
        assertNotNull(results, "Search results should not be null");
    }

    // ==================== deleteFile() Tests ====================

    @Test
    @DisplayName("Negative: deleteFile with non-existent ID should return false")
    public void testDeleteNonExistentFile() {
        boolean result = editorBO.deleteFile(-999);
        assertFalse(result, "Deleting non-existent file should return false");
    }
}
