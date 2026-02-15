package presentation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

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
 * Tests the GUI initialization and window properties.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EditorPOTest {

    private static EditorPO editorPO;

    private static EditorPO getEditor() {
        if (editorPO == null) {
            IEditorDBDAO editorDAO = AbstractDAOEditorFactory.getInstance().createEditorDAO();
            IFacadeDAO facadeDAO = new FacadeDAO(editorDAO);
            IFacadeBO facadeBO = new FacadeBO(new EditorBO(facadeDAO));
            editorPO = new EditorPO(facadeBO);
        }
        return editorPO;
    }

    @Test
    @Order(1)
    @DisplayName("Positive: EditorPO should initialize without throwing exceptions")
    public void testEditorPOInitialization() {
        assertDoesNotThrow(() -> {
            EditorPO editor = getEditor();
            assertNotNull(editor);
        }, "EditorPO should initialize without errors");
    }

    @Test
    @Order(2)
    @DisplayName("Positive: EditorPO should be visible after creation")
    public void testEditorPOIsVisible() {
        EditorPO editor = getEditor();
        assertTrue(editor.isVisible(), "EditorPO frame should be visible after creation");
    }

    @Test
    @Order(3)
    @DisplayName("Positive: EditorPO title should be 'Real Text Editor'")
    public void testEditorPOTitle() {
        EditorPO editor = getEditor();
        assertEquals("Real Text Editor", editor.getTitle(),
                "Title should be 'Real Text Editor'");
    }

    @Test
    @Order(4)
    @DisplayName("Positive: EditorPO default close operation should be EXIT_ON_CLOSE")
    public void testDefaultCloseOperation() {
        EditorPO editor = getEditor();
        assertEquals(javax.swing.JFrame.EXIT_ON_CLOSE, editor.getDefaultCloseOperation(),
                "Default close operation should be EXIT_ON_CLOSE");
        // Clean up
        editor.setVisible(false);
        editor.dispose();
        editorPO = null;
    }
}
