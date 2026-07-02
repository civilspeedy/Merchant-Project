package com.example.database.records;

import com.example.util.Errors;

public record Inventory(
    int userId,
    String code,
    String exchange,
    double quantity
) implements InputRecord {
    public Inventory {
        Errors.checkInventoryRecord(userId, code, exchange, quantity);
    }

    @Override
    public String getFieldString() {
        return new StringBuilder(String.valueOf(userId))
            .append(',')
            .append(code)
            .append(',')
            .append(exchange)
            .append(',')
            .append(quantity)
            .toString();
    }

    @Override
    public String getReplacementString() {
        return "?, ?, ?, ?";
    }
}
