package com.example.database;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.database.records.Inventory;
import com.example.database.records.Transaction;
import com.example.database.records.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;CIPHER=AES";
    private static final String TEST_DB_PASSWORD = "filepwd userpwd";
    private static final String TEST_PASSWORD =
        "qA&!RmCQzCRs7c3bdwvQR7FHQ3NjB!eQsY$9vd64b%^3kemFS8pm#%RtSpD*dvyu";
    private static final int SAFE_USER_ID = 1;
    private static final String SAFE_CODE = "APPL";
    private static final String SAFE_EXCHANGE = "NASDAQ";
    private static final double SAFE_DOUBLE = 1.5324;
    private static final String SAFE_DUB_STR = String.valueOf(SAFE_DOUBLE);
    private static final String SAFE_USER_STR = String.valueOf(SAFE_USER_ID);
    private static LocalDateTime expectedTimestamp;

    @BeforeAll
    public static void setup() throws Exception {
        Database.connect(TEST_DB_URL, TEST_DB_PASSWORD);
        Database.createTables();

        var user = new User(TEST_PASSWORD);
        Database.insert(Database.Insert.USER, user);

        var inventory = new Inventory(SAFE_CODE, SAFE_EXCHANGE, SAFE_DOUBLE);
        Database.insert(Database.Insert.INVENT, inventory);

        var transaction = new Transaction(
            SAFE_CODE,
            SAFE_EXCHANGE,
            SAFE_DOUBLE,
            SAFE_DOUBLE,
            true
        );

        Database.insert(Database.Insert.TRANS, transaction);
        expectedTimestamp = LocalDateTime.now();
    }

    @Test
    public void testSelectUserPassword() throws Exception {
        String[] results = Database.select(Database.Select.PASSWORD, null);
        assertArrayEquals(new String[] { TEST_PASSWORD }, results);
    }

    @Test
    public void testSelectAllInventory() throws Exception {
        String[] results = Database.select(Database.Select.ALL_INVENT, null);
        if (results.length > 1) {
            throw new Exception("results too big");
        }

        String result = results[0];
        String[] actualArray = result.split(",");

        var expectedArray = new String[] {
            SAFE_USER_STR,
            SAFE_CODE,
            SAFE_EXCHANGE,
            SAFE_DUB_STR,
        };

        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testSelectAllTransactions() throws Exception {
        String[] results = Database.select(Database.Select.ALL_TRANS, null);

        if (results.length > 1) {
            throw new Exception("results too big");
        }

        String result = results[0];
        String[] actualArray = result.split(",");
        int numVals = actualArray.length;
        var expectedArray = new String[] {
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
                String[] expectedDate = expectedTimestamp.toString().split("T");
                String[] actualDate = actualArray[i].split(" ");
                assertEquals(expectedDate[0], actualDate[0]);
                // maybe do time but would be hard to get accurate
            } else {
                assertEquals(expectedArray[i], actualArray[i]);
            }
        }
    }
}
