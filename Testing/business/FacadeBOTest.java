package business;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.EditorBO;
import bll.FacadeBO;
import bll.IFacadeBO;
import dal.FacadeDAO;
import dal.IEditorDBDAO;
import dal.IFacadeDAO;
import dal.AbstractDAOEditorFactory;
import dto.Documents;

/**
 * JUnit 5 tests for FacadeBO (Business Layer).
 * Verifies that the Facade pattern correctly delegates to EditorBO.
 * Tests designed against the IFacadeBO interface for swappability.
 */
public class FacadeBOTest {

    private IFacadeBO facadeBO;

    @BeforeEach
    public void setUp() {
        IEditorDBDAO editorDAO = AbstractDAOEditorFactory.getInstance().createEditorDAO();
        IFacadeDAO facadeDAO = new FacadeDAO(editorDAO);
        facadeBO = new FacadeBO(new EditorBO(facadeDAO));
    }

    // ==================== Facade Delegation Tests ====================

    @Test
    @DisplayName("Positive: FacadeBO.getAllFiles() should delegate and return non-null list")
    public void testGetAllFilesDelegation() {
        List<Documents> docs = facadeBO.getAllFiles();
        assertNotNull(docs, "FacadeBO should delegate getAllFiles and return non-null");
    }

    @Test
    @DisplayName("Positive: FacadeBO.getFileExtension() should delegate correctly")
    public void testGetFileExtensionDelegation() {
        String ext = facadeBO.getFileExtension("file.txt");
        assertEquals("txt", ext, "FacadeBO should delegate getFileExtension correctly");
    }

    @Test
    @DisplayName("Positive: FacadeBO.getFileExtension() for .md should return 'md'")
    public void testGetFileExtensionMd() {
        String ext = facadeBO.getFileExtension("notes.md");
        assertEquals("md", ext);
    }

    @Test
    @DisplayName("Negative: FacadeBO.getFile() with invalid ID should return null")
    public void testGetFileInvalidId() {
        Documents doc = facadeBO.getFile(-1);
        assertNull(doc, "Invalid file ID should return null via Facade");
    }

    @Test
    @DisplayName("Negative: FacadeBO.deleteFile() with non-existent ID should return false")
    public void testDeleteNonExistentFile() {
        boolean result = facadeBO.deleteFile(-999);
        assertFalse(result, "Deleting non-existent file should return false");
    }

    @Test
    @DisplayName("Negative: FacadeBO.searchKeyword() short keyword should throw exception")
    public void testSearchKeywordShortThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            facadeBO.searchKeyword("ab");
        }, "Short keyword through Facade should still throw exception");
    }

    @Test
    @DisplayName("Positive: FacadeBO.searchKeyword() valid keyword should not throw")
    public void testSearchKeywordValidNotThrow() {
        assertDoesNotThrow(() -> {
            facadeBO.searchKeyword("test");
        }, "Valid keyword through Facade should not throw exception");
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: FacadeBO.getFileExtension() with no extension returns empty")
    public void testNoExtension() {
        String ext = facadeBO.getFileExtension("noextension");
        assertEquals("", ext, "No extension should return empty string via Facade");
    }

    @Test
    @DisplayName("Boundary: FacadeBO.getFile(0) should return null")
    public void testGetFileZeroId() {
        assertNull(facadeBO.getFile(0), "File ID 0 should return null");
    }
}

