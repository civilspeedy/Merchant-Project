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
    public String[] getFieldArray() {
        return new String[] {
            String.valueOf(userId),
            code,
            exchange,
            String.valueOf(quantity),
        };
    }
}
