package com.example.database.records;

public record User(String username, String password) implements InputRecord {
    public User {
        if (username.length() > 50) throw new IllegalArgumentException(
            "username length cannot exceed 50"
        );
        else if (password.length() > 90) throw new IllegalArgumentException(
            "password length cannot exceed 90"
        );
        else if (
            username.length() == 0 || username == null
        ) throw new IllegalArgumentException(
            "username cannot be empty or null"
        );
        else if (
            password.length() == 0 || password == null
        ) throw new IllegalArgumentException(
            "password cannot be empty or null"
        );
    }

    @Override
    public String getFieldString() {
        return username + ", " + password;
    }

    @Override
    public String getReplacementString() {
        return "?, ?";
    }
}
