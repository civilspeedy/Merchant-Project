package com.example.database.records;

import com.example.util.Errors;

public record Transaction(
    int userId,
    String code,
    String exchange,
    double quantity,
    double price,
    boolean buy
) implements InputRecord {
    public Transaction {
        Errors.checkTransactionRecord(userId, code, exchange, quantity, price);
    }

    @Override
    public String[] getFieldArray() {
        return new String[] {
            String.valueOf(userId),
            code,
            exchange,
            String.valueOf(quantity),
            String.valueOf(price),
            String.valueOf(buy),
        };
    }
}
