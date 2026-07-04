package com.example.database.records;

import com.example.util.Errors;
import java.time.LocalDateTime;

public record Transaction(
    int userId,
    String code,
    String exchange,
    double quantity,
    double price,
    LocalDateTime timestamp,
    boolean buy
) implements InputRecord {
    public Transaction {
        Errors.checkTransactionRecord(
            userId,
            code,
            exchange,
            quantity,
            price,
            timestamp
        );
    }

    @Override
    public String[] getFieldArray() {
        return new String[] {
            String.valueOf(userId),
            code,
            exchange,
            String.valueOf(quantity),
            String.valueOf(price),
            timestamp.toString(),
            String.valueOf(buy),
        };
    }
}
