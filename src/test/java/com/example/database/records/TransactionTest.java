package com.example.database.records;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    private static final String BIG_STR =
        "This is a string that will be way too big for this record in any field.";
    private static Transaction testTransaction;
    private static LocalDateTime expectedTimestamp;

    /**
     * Creates a normal non-exception throwing {@link Transaction} record.
     * @throws IllegalArgumentException
     */
    private static final void getNormal() throws IllegalArgumentException {
        expectedTimestamp = LocalDateTime.now();
        testTransaction = new Transaction(
            10,
            "AAPL",
            "NASDAQ",
            30,
            200.8,
            expectedTimestamp,
            true
        );
    }

    @BeforeAll
    public static void setup() {
        getNormal();
    }

    @Test
    public void testFields() {
        assertEquals(testTransaction.userId(), 10);
        assertEquals(testTransaction.code(), "AAPL");
        assertEquals(testTransaction.exchange(), "NASDAQ");
        assertEquals(testTransaction.quantity(), 30);
        assertEquals(testTransaction.price(), 200.8);
        assertEquals(testTransaction.timestamp(), expectedTimestamp);
        assertEquals(testTransaction.buy(), true);
    }

    @Test
    public void testGetFieldArray() {
        String[] expectArray = new String[] {
            "10",
            "AAPL",
            "NASDAQ",
            "30.0",
            "200.8",
            expectedTimestamp.toString(),
            "true",
        };

        assertArrayEquals(expectArray, testTransaction.getFieldArray());
    }

    @Test
    public void userIdTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                Integer.MIN_VALUE,
                "AAPL",
                "NASDAQ",
                30.0,
                200.8,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals("userId cannot be less than zero", ex.getMessage());
    }

    @Test
    public void codeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                BIG_STR,
                "NASDAQ",
                30.0,
                200.8,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals("code length cannot exceed 5", ex.getMessage());
    }

    @Test
    public void codeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "",
                "NASDAQ",
                30.0,
                200.8,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals("code cannot be empty or null ", ex.getMessage());
    }

    @Test
    public void exchangeTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "AAPL",
                BIG_STR,
                30.0,
                200.8,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals("exchange length cannot exceed 12", ex.getMessage());
    }

    @Test
    public void exchangeTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "AAPL",
                "",
                30.0,
                200.8,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals("exchange cannot be empty or null ", ex.getMessage());
    }

    @Test
    public void quantityTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "AAPL",
                "NASDAQ",
                Double.MAX_VALUE,
                200.8,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals("quantity cannot exceed 1.0E38", ex.getMessage());
    }

    @Test
    public void quantityTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "AAPL",
                "NASDAQ",
                Double.NEGATIVE_INFINITY,
                200.8,
                LocalDateTime.now(),
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
            new Transaction(
                10,
                "AAPL",
                "NASDAQ",
                30.0,
                Double.MAX_VALUE,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals("price cannot exceed 1.0E38", ex.getMessage());
    }

    @Test
    public void priceTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "AAPL",
                "NASDAQ",
                30.0,
                Double.NEGATIVE_INFINITY,
                LocalDateTime.now(),
                true
            );
        });
        assertEquals(
            "price cannot be less than or equal to 0 ",
            ex.getMessage()
        );
    }

    @Test
    public void timestampTooBigException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "AAPL",
                "NASDAQ",
                30.0,
                200.8,
                LocalDateTime.parse("2100-01-01T12:00:00"),
                true
            );
        });
        assertEquals("timestamps cannot be in the future", ex.getMessage());
    }

    @Test
    public void timestampTooSmallException() {
        var ex = assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                10,
                "AAPL",
                "NASDAQ",
                30.0,
                200.8,
                LocalDateTime.parse("1990-01-01T12:00:00"),
                true
            );
        });
        assertEquals(
            "timestamp cannot be before application existence",
            ex.getMessage()
        );
    }
}
