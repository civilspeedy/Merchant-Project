package com.example.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.val;

public final class Log {

    public static boolean enabled = true;
    private static final DateTimeFormatter timeFormatter =
        DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void out(String msg) {
        if (enabled) {
            val time = LocalTime.now();
            val message = String.format(
                "[LOG] %s %s",
                msg,
                time.format(timeFormatter)
            );
            System.out.println(message);
        }
    }
}
