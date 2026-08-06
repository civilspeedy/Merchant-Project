package com.example.database.records;

import com.example.util.Errors;

public record Inventory(
    String code,
    String exchange,
    double quantity
) implements InputRecord {
    public Inventory {
        Errors.checkInventoryRecord(code, exchange, quantity);
    }

    @Override
    public String[] getFieldArray() {
        return new String[] { code, exchange, String.valueOf(quantity) };
    }
}
