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
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(Integer.MIN_VALUE, BLANK, BLANK, 1.0);
        });
        assertEquals("userId cannot be less than zero", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when code string is too big.
     */
    @Test
    public void codeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, STR_TOO_BIG, BLANK, 1.0);
        });
        assertEquals("code length cannot exceed 5", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when code string is too small.
     */
    @Test
    public void codeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, "", BLANK, 1.0);
        });
        assertEquals("code cannot be empty or null ", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when exchange string is too big.
     */
    @Test
    public void exchangeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, STR_TOO_BIG, 1.0);
        });
        assertEquals("exchange length cannot exceed 12", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when exchange string is too small.
     */
    @Test
    public void exchangeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, "", 1.0);
        });
        assertEquals("exchange cannot be empty or null ", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when quantity is too big.
     */
    @Test
    public void quantityTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, BLANK, Double.MAX_VALUE);
        });
        assertEquals("quantity cannot exceed 1.0E38", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when quantity is too small.
     */
    @Test
    public void quantityTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(0, BLANK, BLANK, Double.NEGATIVE_INFINITY);
        });
        assertEquals(
            "quantity cannot be less than or equal to 0 ",
            ex.getMessage()
        );
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
