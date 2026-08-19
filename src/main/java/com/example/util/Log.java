package com.example.util;

import java.util.HashMap;
import lombok.NonNull;
import lombok.val;

public final class Log {

    public static boolean enabled = true;
    private static final double NS_IN_MS = 1_000_000.0;
    private static final String SET_CYAN = "\033[1;36m";
    private static final String RESET = "\033[0m";
    private static final String SET_BLUE = "\033[1;34m";
    private static final String SET_GREEN = "\033[1;32m";

    private static HashMap<String, Long> timeMap = new HashMap<>();

    private static long time() {
        return System.nanoTime();
    }

    public static void start(@NonNull String key) {
        timeMap.put(key, time());
    }

    public static void clear(@NonNull String key) {
        timeMap.remove(key);
    }

    public static void stop(@NonNull String key) {
        long mem =
            (Runtime.getRuntime().totalMemory() -
                Runtime.getRuntime().freeMemory()) >>
            20;

        Long start = timeMap.get(key);
        if (start == null || start.longValue() == 0l) {
            throw new IllegalStateException("start time has not been recorded");
        }

        double delta = (time() - start.longValue()) / NS_IN_MS;

        val msg = String.format(
            "[%sLOG%s] %s | %sΔ: %.2fms%s | %sMem: %d mb%s",
            SET_CYAN,
            RESET,
            key,
            SET_BLUE,
            delta,
            RESET,
            SET_GREEN,
            mem,
            RESET
        );

        clear(key);
        System.out.println(msg);
    }
}
