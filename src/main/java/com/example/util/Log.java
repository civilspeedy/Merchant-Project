package com.example.util;

import lombok.val;

public final class Log {

    public static boolean enabled = true;
    private static final double NS_IN_MS = 1_000_000.0;
    private static long previousTime = time();
    private static final String SET_CYAN = "\033[1;36m";
    private static final String RESET = "\033[0m";
    private static final String SET_BLUE = "\033[1;34m";
    private static boolean firstLog = true;

    private static long time() {
        return System.nanoTime();
    }

    public static void out(String msg) {
        if (enabled) {
            long time = time();
            double delta = (time - previousTime) / NS_IN_MS;
            val deltaStr = firstLog
                ? ""
                : String.format("| %sΔ: %.2fms%s", SET_BLUE, delta, RESET);
            val message = String.format(
                "\n[%sLOG%s] %s ",
                SET_CYAN,
                RESET,
                msg
            );
            previousTime = time;
            firstLog = false;
            System.out.print(deltaStr + message);
        }
    }
}
