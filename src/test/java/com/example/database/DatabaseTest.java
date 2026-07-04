package com.example.database;

import com.example.database.records.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;CIPHER=AES";
    private static final String TEST_DB_PASSWORD = "filepwd userpwd";

    @BeforeAll
    public static void setup() throws Exception {
        Database.connect(TEST_DB_URL, TEST_DB_PASSWORD);
        Database.createTables();
    }

    @Test
    public void testUserInsert() throws Exception {
        User user = new User("charlie", "superDooperPassword");
        Database.insert(user);
        // check via select
    }
}
