package com.example.database.records;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InventoryTest {

    private static final String BLANK = "00000";
    private static final String STR_TOO_BIG = "This is a string value too big!";
    private static final double SAFE_DOUBLE = 1.6;
    private static Inventory testInventory;

    @BeforeAll
    public static void setUp() {
        testInventory = new Inventory("AAPL", "NASDAQ", SAFE_DOUBLE);
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when code string is too big.
     */
    @Test
    public void codeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(STR_TOO_BIG, BLANK, SAFE_DOUBLE);
        });
        assertEquals("code length cannot exceed 5", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when code string is too small.
     */
    @Test
    public void codeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory("", BLANK, SAFE_DOUBLE);
        });
        assertEquals("code cannot be empty or null ", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when exchange string is too big.
     */
    @Test
    public void exchangeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(BLANK, STR_TOO_BIG, SAFE_DOUBLE);
        });
        assertEquals("exchange length cannot exceed 12", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when exchange string is too small.
     */
    @Test
    public void exchangeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(BLANK, "", SAFE_DOUBLE);
        });
        assertEquals("exchange cannot be empty or null ", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when quantity is too big.
     */
    @Test
    public void quantityTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(BLANK, BLANK, Double.MAX_VALUE);
        });
        assertEquals("quantity cannot exceed 1.0E38", ex.getMessage());
    }

    /**
     * Correctly throw an {@link IllegalArgumentException} when quantity is too small.
     */
    @Test
    public void quantityTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Inventory(BLANK, BLANK, Double.NEGATIVE_INFINITY);
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
    public void testGetFieldArray() {
        String[] expectedArray = new String[] {
            "AAPL",
            "NASDAQ",
            String.valueOf(SAFE_DOUBLE),
        };
        assertArrayEquals(expectedArray, testInventory.getFieldArray());
    }
}
