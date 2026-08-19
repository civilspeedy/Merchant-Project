package com.example.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    private static final String TEST_PASSWORD =
        "qA&!RmCQzCRs7c3bdwvQR7FHQ3NjB!eQsY$9vd64b%^3kemFS8pm#%RtSpD*dvyu";

    @BeforeAll
    public static void setup() throws SQLException {
        // Create a new user (this also initializes the database)
        Database.newUser(TEST_PASSWORD, "need api here");
    }

    @Test
    public void testNewUser() throws SQLException {
        // Verify that the database exists after newUser() is called
        assertTrue(
            Database.dbExists(),
            "Database should exist after newUser()"
        );
    }

    @Test
    public void testLogin() throws SQLException {
        // Test that login succeeds with correct password
        // (setup already logged in, so we verify no exception is thrown)
        Database.login(TEST_PASSWORD);
        // If no exception thrown, login was successful
        assertTrue(true);
    }

    @Test
    public void testDbExists() {
        // Verify database existence
        assertTrue(Database.dbExists(), "Database should exist after setup");
    }

    @Test
    public void testNewApiKey() throws SQLException {
        String apiKeyName = "test_key";
        String apiKeyValue = "secret-key-12345";

        // Insert a new API key
        Database.newApiKey(apiKeyName, apiKeyValue);

        // Retrieve the API key and verify it matches
        String retrievedKey = Database.getApiKey(apiKeyName);
        assertEquals(
            apiKeyValue,
            retrievedKey,
            "Retrieved API key should match the inserted one"
        );
    }

    @Test
    public void testGetApiKey() throws SQLException {
        String apiKeyName = "test_key_2";
        String apiKeyValue = "another-secret-key";

        Database.newApiKey(apiKeyName, apiKeyValue);
        String retrieved = Database.getApiKey(apiKeyName);

        assertEquals(
            apiKeyValue,
            retrieved,
            "API key should be retrievable after insertion"
        );
    }

    @Test
    public void testGetApiKeyNotFound() throws SQLException {
        // Attempting to get a non-existent API key should throw SQLException
        assertThrows(
            SQLException.class,
            () -> {
                Database.getApiKey("non-existent-key");
            },
            "Should throw SQLException when API key not found"
        );
    }

    @AfterAll
    public static void finishUp() {
        Database.destroyDb();
    }
}
