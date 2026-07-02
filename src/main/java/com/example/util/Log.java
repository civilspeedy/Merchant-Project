package com.example.util;

public class Log {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RED = "\u001B[31m";

    private String whereStr;
    private static int count = 0;

    public Log(String where) {
        this.whereStr = where;
    }

    private String buildMsg(String msg, boolean isErr) {
        count++;
        return new StringBuilder(count + " ")
            .append(isErr ? ANSI_RED : ANSI_CYAN)
            .append(this.whereStr)
            .append(':')
            .append(msg)
            .append(ANSI_RESET)
            .toString();
    }

    public void out(String msg) {
        System.out.println(this.buildMsg(msg, false));
    }

    public void err(String msg) {
        System.out.println(this.buildMsg(msg, true));
        System.exit(-1);
    }

    public void err(Exception msg) {
        this.err(msg.getCause().toString() + "! " + msg.getMessage());
        msg.printStackTrace();
        System.exit(-1);
    }
}
