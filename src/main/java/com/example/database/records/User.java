package com.example.database.records;

public record User(String password) implements InputRecord {
    private static final int MAX_CHARS = 90;

    public User {
        if (password.length() > MAX_CHARS) throw new IllegalArgumentException(
            "password length cannot exceed 90"
        );
        else if (
            password.isBlank() || password.isEmpty()
        ) throw new IllegalArgumentException(
            "password cannot be empty or null"
        );
    }

    @Override
    public String[] getFieldArray() {
        return new String[] { password };
    }
}
