package com.example.database.records;

import com.example.util.Errors;

public record Transaction(
    String code,
    String exchange,
    double quantity,
    double price,
    boolean buy
) implements InputRecord {
    public Transaction {
        Errors.checkTransactionRecord(code, exchange, quantity, price);
    }

    @Override
    public String[] getFieldArray() {
        return new String[] {
            code,
            exchange,
            String.valueOf(quantity),
            String.valueOf(price),
            String.valueOf(buy),
        };
    }
}
