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
    public String getFieldString() {
        return new StringBuilder(String.valueOf(userId))
            .append(',')
            .append(code)
            .append(',')
            .append(exchange)
            .append(',')
            .append(timestamp.toString())
            .append(',')
            .append(buy)
            .toString();
    }

    @Override
    public String getReplacementString() {
        return "?, ?, ?, ?, ?, ?, ?";
    }
}
