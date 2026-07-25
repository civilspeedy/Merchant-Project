package com.example.util;

public final class Errors {

    private static final String TOO_SMALL =
        " cannot be less than or equal to 0 ";
    private static final String EMPTY_NULL = " cannot be empty or null ";

    /**
     * Max value a SQL DECIMAL value can be.
     */
    private static final double DECIMAL_MAX = 1e+38;

    private static final int CODE_MAX = 5;
    private static final int EX_MAX = 12;

    public static final void checkInventoryRecord(
        String code,
        String exchange,
        double quantity
    ) throws IllegalArgumentException {
        checkCode(code);
        checkExchange(exchange);
        checkQuantity(quantity);
    }

    private static final void checkCode(String code)
        throws IllegalArgumentException {
        if (code.length() > CODE_MAX) {
            throw new IllegalArgumentException(
                "code length cannot exceed " + CODE_MAX
            );
        } else if (code.length() == 0 || code == null) {
            throw new IllegalArgumentException("code" + EMPTY_NULL);
        }
    }

    private static final void checkExchange(String exchange)
        throws IllegalArgumentException {
        if (exchange.length() > EX_MAX) {
            throw new IllegalArgumentException(
                "exchange length cannot exceed " + EX_MAX
            );
        } else if (exchange.length() == 0 || exchange == null) {
            throw new IllegalArgumentException("exchange" + EMPTY_NULL);
        }
    }

    private static final void checkQuantity(double quantity)
        throws IllegalArgumentException {
        if (quantity > DECIMAL_MAX) {
            throw new IllegalArgumentException(
                "quantity cannot exceed " + DECIMAL_MAX
            );
        } else if (quantity <= 0) {
            throw new IllegalArgumentException("quantity" + TOO_SMALL);
        }
    }

    public static final void checkTransactionRecord(
        String code,
        String exchange,
        double quantity,
        double price
    ) throws IllegalArgumentException {
        checkInventoryRecord(code, exchange, quantity);
        checkPrice(price);
    }

    private static final void checkPrice(double price)
        throws IllegalArgumentException {
        if (price > DECIMAL_MAX) {
            throw new IllegalArgumentException(
                "price cannot exceed " + DECIMAL_MAX
            );
        } else if (price <= 0) {
            throw new IllegalArgumentException("price" + TOO_SMALL);
        }
    }
}
