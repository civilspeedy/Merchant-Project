package com.example.database;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.database.records.Inventory;
import com.example.database.records.Transaction;
import com.example.database.records.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;CIPHER=AES";
    private static final String TEST_DB_PASSWORD = "filepwd userpwd";
    private static final String TEST_USERNAME = "Charlie";
    private static final String TEST_PASSWORD =
        "qA&!RmCQzCRs7c3bdwvQR7FHQ3NjB!eQsY$9vd64b%^3kemFS8pm#%RtSpD*dvyu";
    private static final int SAFE_USER_ID = 1;
    private static final String SAFE_CODE = "APPL";
    private static final String SAFE_EXCHANGE = "NASDAQ";
    private static final double SAFE_DOUBLE = 1.5324;
    private static final String SAFE_DUB_STR = String.valueOf(SAFE_DOUBLE);
    private static final String SAFE_USER_STR = String.valueOf(SAFE_USER_ID);
    private static LocalDateTime expectedDate;

    private static final boolean contains(String[] arr, String target) {
        return Arrays.stream(arr).anyMatch(target::equals);
    }

    @BeforeAll
    public static void setup() throws Exception {
        Database.connect(TEST_DB_URL, TEST_DB_PASSWORD);
        Database.createTables();

        var user = new User(TEST_USERNAME, TEST_PASSWORD);
        Database.insert(Database.Insert.USER, user);

        var inventory = new Inventory(
            SAFE_USER_ID,
            SAFE_CODE,
            SAFE_EXCHANGE,
            SAFE_DOUBLE
        );
        Database.insert(Database.Insert.INVENT, inventory);

        var transaction = new Transaction(
            SAFE_USER_ID,
            SAFE_CODE,
            SAFE_EXCHANGE,
            SAFE_DOUBLE,
            SAFE_DOUBLE,
            true
        );

        Database.insert(Database.Insert.TRANS, transaction);
        expectedDate = LocalDateTime.now();
    }

    @Test
    public void testSelectUsernames() throws Exception {
        String[] results = Database.select(Database.Select.USERNAMES, null);
        assertArrayEquals(new String[] { TEST_USERNAME }, results);
    }

    @Test
    public void testSelectUserId() throws Exception {
        String[] results = Database.select(
            Database.Select.USER_ID,
            TEST_USERNAME
        );
        assertArrayEquals(new String[] { SAFE_USER_STR }, results);
    }

    @Test
    public void testSelectUserPassword() throws Exception {
        String[] results = Database.select(
            Database.Select.PASSWORD,
            TEST_USERNAME
        );
        assertArrayEquals(new String[] { TEST_PASSWORD }, results);
    }

    @Test
    public void testSelectAllTransactions() throws Exception {
        String[] results = Database.select(
            Database.Select.ALL_TRANS,
            SAFE_USER_STR
        );

        if (results.length > 1) {
            throw new Exception("return too big");
        }

        String result = results[0];
        String[] values = result.split(",");
        int numVals = values.length;
        String[] expectedArray = new String[] {
            SAFE_USER_STR,
            SAFE_USER_STR,
            SAFE_CODE,
            SAFE_EXCHANGE,
            SAFE_DUB_STR,
            SAFE_DUB_STR,
            "TIMESTAMP",
            "TRUE",
        };

        if (numVals != expectedArray.length) {
            throw new Exception("too many fields");
        }

        for (int i = 0; i < numVals; i++) {
            if (expectedArray[i].equals("TIMESTAMP")) {
                // TODO
            } else {
                assertEquals(expectedArray[i], values[i]);
            }
        }
    }
}
