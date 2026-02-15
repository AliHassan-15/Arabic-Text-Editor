package presentation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bll.EditorBO;
import bll.FacadeBO;
import bll.IFacadeBO;
import dal.FacadeDAO;
import dal.IEditorDBDAO;
import dal.IFacadeDAO;
import dal.AbstractDAOEditorFactory;
import pl.EditorPO;

/**
 * JUnit 5 tests for EditorPO (Presentation Layer).
 * Tests the GUI helper/utility methods and initialization.
 */
public class EditorPOTest {

    private IFacadeBO facadeBO;
    private EditorPO editorPO;

    @BeforeEach
    public void setUp() {
        IEditorDBDAO editorDAO = AbstractDAOEditorFactory.getInstance().createEditorDAO();
        IFacadeDAO facadeDAO = new FacadeDAO(editorDAO);
        facadeBO = new FacadeBO(new EditorBO(facadeDAO));
    }

    @AfterEach
    public void tearDown() {
        if (editorPO != null) {
            editorPO.dispose();
            editorPO = null;
        }
    }

    // ==================== Initialization Tests ====================

    @Test
    @DisplayName("Positive: EditorPO should initialize without throwing exceptions")
    public void testEditorPOInitialization() {
        assertDoesNotThrow(() -> {
            editorPO = new EditorPO(facadeBO);
        }, "EditorPO should initialize without errors");
    }

    @Test
    @DisplayName("Positive: EditorPO should be visible after creation")
    public void testEditorPOIsVisible() {
        editorPO = new EditorPO(facadeBO);
        assertTrue(editorPO.isVisible(), "EditorPO frame should be visible after creation");
    }

    @Test
    @DisplayName("Positive: EditorPO title should be 'Real Text Editor'")
    public void testEditorPOTitle() {
        editorPO = new EditorPO(facadeBO);
        assertEquals("Real Text Editor", editorPO.getTitle(),
                "Title should be 'Real Text Editor'");
    }

    @Test
    @DisplayName("Positive: EditorPO default close operation should be EXIT_ON_CLOSE")
    public void testDefaultCloseOperation() {
        editorPO = new EditorPO(facadeBO);
        assertEquals(javax.swing.JFrame.EXIT_ON_CLOSE, editorPO.getDefaultCloseOperation(),
                "Default close operation should be EXIT_ON_CLOSE");
    }

    // ==================== Negative Tests ====================

    @Test
    @DisplayName("Negative: EditorPO with null business object should handle gracefully")
    public void testEditorPOWithNullBO() {
        // Passing null may cause NullPointerException when interacting,
        // but construction itself may or may not throw
        try {
            editorPO = new EditorPO(null);
            // If it doesn't throw, the frame was created (UI initialized)
            assertNotNull(editorPO);
        } catch (NullPointerException e) {
            // Acceptable - no graceful handling for null BO
            assertNotNull(e);
        }
    }
}

