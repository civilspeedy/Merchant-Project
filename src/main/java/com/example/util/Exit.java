package com.example.util;

public enum Exit {
    ERR(-1),
    OK(0);

    public int code;

    private Exit(int code) {
        this.code = code;
    }
}
