package business;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
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
 * JUnit 5 tests for Import/Export commands (Business Layer).
 * Tests importTextFiles and file creation logic.
 */
public class ImportExportTest {

    private IEditorBO editorBO;
    private File tempTxtFile;
    private File tempMdFile;
    private File tempInvalidFile;

    @BeforeEach
    public void setUp() throws IOException {
        IEditorDBDAO editorDAO = AbstractDAOEditorFactory.getInstance().createEditorDAO();
        IFacadeDAO facadeDAO = new FacadeDAO(editorDAO);
        editorBO = new EditorBO(facadeDAO);

        // Create temp .txt file
        tempTxtFile = File.createTempFile("test_import", ".txt");
        FileWriter writer1 = new FileWriter(tempTxtFile);
        writer1.write("This is a test text file for import testing.");
        writer1.close();

        // Create temp .md file
        tempMdFile = File.createTempFile("test_import", ".md");
        FileWriter writer2 = new FileWriter(tempMdFile);
        writer2.write("# Markdown Test\nThis is markdown content.");
        writer2.close();

        // Create temp .pdf file (unsupported)
        tempInvalidFile = File.createTempFile("test_import", ".pdf");
        FileWriter writer3 = new FileWriter(tempInvalidFile);
        writer3.write("fake pdf content");
        writer3.close();
    }

    @AfterEach
    public void tearDown() {
        if (tempTxtFile != null) tempTxtFile.delete();
        if (tempMdFile != null) tempMdFile.delete();
        if (tempInvalidFile != null) tempInvalidFile.delete();
    }

    // ==================== getFileExtension Tests ====================

    @Test
    @DisplayName("Positive: .txt extension should be detected correctly")
    public void testTxtExtension() {
        assertEquals("txt", editorBO.getFileExtension("document.txt"));
    }

    @Test
    @DisplayName("Positive: .md extension should be detected correctly")
    public void testMdExtension() {
        assertEquals("md", editorBO.getFileExtension("readme.md"));
    }

    @Test
    @DisplayName("Negative: .pdf extension should be detected but not supported for import")
    public void testPdfExtension() {
        assertEquals("pdf", editorBO.getFileExtension("report.pdf"));
    }

    @Test
    @DisplayName("Negative: .exe extension should be detected but not supported")
    public void testExeExtension() {
        assertEquals("exe", editorBO.getFileExtension("program.exe"));
    }

    // ==================== importTextFiles Tests ====================

    @Test
    @DisplayName("Positive: Import .txt file should succeed")
    public void testImportTxtFile() {
        boolean result = editorBO.importTextFiles(tempTxtFile, tempTxtFile.getName());
        assertTrue(result, "Importing a .txt file should succeed");
    }

    @Test
    @DisplayName("Positive: Import .md file should succeed")
    public void testImportMdFile() {
        boolean result = editorBO.importTextFiles(tempMdFile, tempMdFile.getName());
        assertTrue(result, "Importing a .md file should succeed");
    }

    @Test
    @DisplayName("Negative: Import .pdf file should fail (unsupported)")
    public void testImportPdfFileFails() {
        boolean result = editorBO.importTextFiles(tempInvalidFile, tempInvalidFile.getName());
        assertFalse(result, "Importing a .pdf file should fail");
    }

    @Test
    @DisplayName("Negative: Import non-existent file should fail gracefully")
    public void testImportNonExistentFile() {
        File fakeFile = new File("nonexistent_file.txt");
        boolean result = editorBO.importTextFiles(fakeFile, "nonexistent_file.txt");
        assertFalse(result, "Importing non-existent file should return false");
    }

    // ==================== createFile Tests ====================

    @Test
    @DisplayName("Positive: createFile with valid name and content should succeed")
    public void testCreateFileValid() {
        String uniqueName = "testfile_" + System.currentTimeMillis() + ".txt";
        boolean result = editorBO.createFile(uniqueName, "Test content for create");
        assertTrue(result, "Creating a file with valid inputs should succeed");
    }

    @Test
    @DisplayName("Boundary: createFile with empty content should still succeed")
    public void testCreateFileEmptyContent() {
        String uniqueName = "emptyfile_" + System.currentTimeMillis() + ".txt";
        boolean result = editorBO.createFile(uniqueName, "");
        assertTrue(result, "Creating a file with empty content should succeed");
    }
}

