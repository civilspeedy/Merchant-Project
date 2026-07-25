package com.example.database.records;

public record User(String password) implements InputRecord {
    public User {
        if (password.length() > 90) throw new IllegalArgumentException(
            "password length cannot exceed 90"
        );
        else if (
            password.length() == 0 || password == null
        ) throw new IllegalArgumentException(
            "password cannot be empty or null"
        );
    }

    @Override
    public String[] getFieldArray() {
        return new String[] { password };
    }
}
