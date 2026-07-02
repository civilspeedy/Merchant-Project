package com.example.database.records;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    private static final String BIG_STR =
        "This is a string that will be way too big for this record in any field.";

    @Test
    public void testConstruct() {
        new Transaction(
            10,
            "AAPL",
            "NASDAQ",
            30.0,
            200.8,
            LocalDateTime.now(),
            true
        );
    }

    @Test
    public void userIdTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void codeTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void codeTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void exchangeTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void exchangeTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void quantityTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void quantityTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void priceTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void priceTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void timestampTooBigException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }

    @Test
    public void timestampTooSmallException() {
        assertThrows(IllegalArgumentException.class, () -> {
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
    }
}
