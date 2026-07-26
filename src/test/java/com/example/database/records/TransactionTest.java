package com.example.database.records;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    private static final String BIG_STR =
        "This is a string that will be way too big for this record in any field.";
    private static Transaction testTransaction;

    /**
     * Creates a normal non-exception throwing {@link Transaction} record.
     * @throws IllegalArgumentException
     */
    private static final void getNormal() throws IllegalArgumentException {
        testTransaction = new Transaction("AAPL", "NASDAQ", 30, 200.8, true);
    }

    @BeforeAll
    public static void setup() {
        getNormal();
    }

    @Test
    public void testFields() {
        assertEquals(testTransaction.code(), "AAPL");
        assertEquals(testTransaction.exchange(), "NASDAQ");
        assertEquals(testTransaction.quantity(), 30);
        assertEquals(testTransaction.price(), 200.8);
        assertEquals(testTransaction.buy(), true);
    }

    @Test
    public void testGetFieldArray() {
        var expectArray = new String[] {
            "AAPL",
            "NASDAQ",
            "30.0",
            "200.8",
            "true",
        };

        assertArrayEquals(expectArray, testTransaction.getFieldArray());
    }

    @Test
    public void codeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                BIG_STR,
                "NASDAQ",
                30.0,
                200.8,

                true
            );
        });
        assertEquals("code length cannot exceed 5", ex.getMessage());
    }

    @Test
    public void codeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("", "NASDAQ", 30.0, 200.8, true);
        });
        assertEquals("code cannot be empty or null ", ex.getMessage());
    }

    @Test
    public void exchangeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("AAPL", BIG_STR, 30.0, 200.8, true);
        });
        assertEquals("exchange length cannot exceed 12", ex.getMessage());
    }

    @Test
    public void exchangeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("AAPL", "", 30.0, 200.8, true);
        });
        assertEquals("exchange cannot be empty or null ", ex.getMessage());
    }

    @Test
    public void quantityTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("AAPL", "NASDAQ", Double.MAX_VALUE, 200.8, true);
        });
        assertEquals("quantity cannot exceed 1.0E38", ex.getMessage());
    }

    @Test
    public void quantityTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                "AAPL",
                "NASDAQ",
                Double.NEGATIVE_INFINITY,
                200.8,
                true
            );
        });
        assertEquals(
            "quantity cannot be less than or equal to 0 ",
            ex.getMessage()
        );
    }

    @Test
    public void priceTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction("AAPL", "NASDAQ", 30.0, Double.MAX_VALUE, true);
        });
        assertEquals("price cannot exceed 1.0E38", ex.getMessage());
    }

    @Test
    public void priceTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                "AAPL",
                "NASDAQ",
                30.0,
                Double.NEGATIVE_INFINITY,
                true
            );
        });
        assertEquals(
            "price cannot be less than or equal to 0 ",
            ex.getMessage()
        );
    }
}
