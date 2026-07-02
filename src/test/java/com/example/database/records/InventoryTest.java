package com.example.database.records;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InventoryTest {

    private static final String BLANK = "00000";
    private static final String STR_TOO_BIG = "This is a string value too big!";
    private static Inventory testInventory;

    @BeforeAll
    public static void setUp() {
        testInventory = new Inventory(0, "AAPL", "NASDAQ", 1.6);
    }

    /**
     * Correctly construct an Inventory record without throwing an {@link IllegalArgumentException}.
     */
    @Test
    public void testConstruct() {
        new Inventory(0, "AAPL", "NASDAQ", 1.6);
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when userId is too small.
     */
    @Test
    public void userIdTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(Integer.MIN_VALUE, BLANK, BLANK, 1.0);
        });
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when code string is too big.
     */
    @Test
    public void codeTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, STR_TOO_BIG, BLANK, 1.0);
        });
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when code string is too small.
     */
    @Test
    public void codeTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, "", BLANK, 1.0);
        });
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when exchange string is too big.
     */
    @Test
    public void exchangeTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, STR_TOO_BIG, 1.0);
        });
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when exchange string is too small.
     */
    @Test
    public void exchangeTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, "", 1.0);
        });
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when quantity is too big.
     */
    @Test
    public void quantityTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, STR_TOO_BIG, Double.MAX_VALUE);
        });
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when quantity is too small.
     */
    @Test
    public void quantityTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, STR_TOO_BIG, Double.MIN_VALUE);
        });
    }

    /**
     * Correctly return the field string representation of the Inventory record.
     */
    @Test
    public void testGetFieldString() {
        assertEquals("0,AAPL,NASDAQ,1.6", testInventory.getFieldString());
    }

    @Test
    public void testGetReplacementString() {
        assertEquals("?, ?, ?, ?", testInventory.getReplacementString());
    }
}
