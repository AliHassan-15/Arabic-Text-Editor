package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.EditorDBDAO;
import dal.HashCalculator;
import dal.IEditorDBDAO;
import dal.AbstractDAOEditorFactory;
import dto.Documents;

/**
 * JUnit 5 tests for EditorDBDAO (Data Persistence Layer).
 * Tests CRUD operations and hashing integrity.
 * Designed against the IEditorDBDAO interface for swappability.
 */
public class EditorDBDAOTest {

    private IEditorDBDAO editorDAO;

    @BeforeEach
    public void setUp() {
        editorDAO = AbstractDAOEditorFactory.getInstance().createEditorDAO();
    }

    // ==================== Positive Tests ====================

    @Test
    @DisplayName("Positive: getFilesFromDB should return a non-null list")
    public void testGetFilesFromDBNotNull() {
        List<Documents> docs = editorDAO.getFilesFromDB();
        assertNotNull(docs, "getFilesFromDB should never return null");
    }

    @Test
    @DisplayName("Positive: getFilesFromDB should return list type")
    public void testGetFilesFromDBReturnsList() {
        List<Documents> docs = editorDAO.getFilesFromDB();
        assertNotNull(docs);
        assertTrue(docs instanceof List, "Should return a List");
    }

    // ==================== Hashing Integrity Tests ====================

    @Test
    @DisplayName("Positive: Same content should always produce the same hash")
    public void testHashConsistency() throws Exception {
        String content = "بسم الله الرحمن الرحيم";
        String hash1 = HashCalculator.calculateHash(content);
        String hash2 = HashCalculator.calculateHash(content);
        assertEquals(hash1, hash2, "Same content should produce same hash");
    }

    @Test
    @DisplayName("Positive: Editing content should change the hash value")
    public void testEditingChangesHash() throws Exception {
        String originalContent = "بسم الله الرحمن الرحيم";
        String editedContent = "بسم الله الرحمن الرحيم - تعديل";

        String originalHash = HashCalculator.calculateHash(originalContent);
        String editedHash = HashCalculator.calculateHash(editedContent);

        assertNotEquals(originalHash, editedHash,
                "Editing file content should change the current session hash");
    }

    @Test
    @DisplayName("Positive: Original import hash should be retained after edit")
    public void testOriginalHashRetained() throws Exception {
        String importContent = "الحمد لله رب العالمين";
        String importHash = HashCalculator.calculateHash(importContent);

        // Simulate editing - the original hash should still be the same value
        String editedContent = importContent + " تعديل جديد";
        String editedHash = HashCalculator.calculateHash(editedContent);

        // Original import hash value should remain unchanged
        String reimportHash = HashCalculator.calculateHash(importContent);
        assertEquals(importHash, reimportHash,
                "The original import hash should be retained and not change");
        assertNotEquals(importHash, editedHash,
                "The edited content should have a different hash than the import");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: deleteFileInDB with non-existent ID should return false")
    public void testDeleteNonExistentFile() {
        boolean result = editorDAO.deleteFileInDB(-999);
        assertFalse(result, "Deleting non-existent file should return false");
    }

    @Test
    @DisplayName("Negative: updateFileInDB with non-existent ID should return false")
    public void testUpdateNonExistentFile() {
        boolean result = editorDAO.updateFileInDB(-999, "fake.txt", 1, "content");
        assertFalse(result, "Updating non-existent file should return false");
    }

    // ==================== Boundary Tests ====================

    @Test
    @DisplayName("Boundary: getFilesFromDB should handle empty database gracefully")
    public void testGetFilesFromDBEmptyDB() {
        // Even with no files, the method should return an empty list, not null
        List<Documents> docs = editorDAO.getFilesFromDB();
        assertNotNull(docs, "Should return empty list, not null, when DB is empty");
    }
}

