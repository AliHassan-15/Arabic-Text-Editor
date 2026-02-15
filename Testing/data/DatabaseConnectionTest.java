package data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dal.DatabaseConnection;

/**
 * JUnit 5 tests for DatabaseConnection (Data Layer).
 * Verifies Singleton design pattern properties.
 */
public class DatabaseConnectionTest {

    // ==================== Singleton Property Tests ====================

    @Test
    @DisplayName("Singleton: getInstance() should never return null")
    public void testInstanceNotNull() {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        assertNotNull(instance, "getInstance() should never return null");
    }

    @Test
    @DisplayName("Singleton: Two calls to getInstance() should return the exact same object")
    public void testSingletonReturnsSameInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        assertSame(instance1, instance2,
                "Both calls to getInstance() must return the exact same object reference");
    }

    @Test
    @DisplayName("Singleton: Multiple sequential calls should all return same instance")
    public void testMultipleCallsReturnSameInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        DatabaseConnection instance3 = DatabaseConnection.getInstance();
        assertSame(instance1, instance2, "Instance 1 and 2 should be same");
        assertSame(instance2, instance3, "Instance 2 and 3 should be same");
        assertSame(instance1, instance3, "Instance 1 and 3 should be same");
    }

    // ==================== Connection Tests ====================

    @Test
    @DisplayName("Positive: getConnection() should return a non-null connection")
    public void testConnectionNotNull() {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        assertNotNull(instance.getConnection(),
                "getConnection() should return a valid connection object");
    }

    @Test
    @DisplayName("Positive: Connection should be open and valid")
    public void testConnectionIsValid() throws Exception {
        DatabaseConnection instance = DatabaseConnection.getInstance();
        java.sql.Connection conn = instance.getConnection();
        assertNotNull(conn, "Connection should not be null");
        assertFalse(conn.isClosed(), "Connection should be open");
    }
}

