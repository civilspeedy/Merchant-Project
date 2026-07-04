package com.example;

import com.example.util.Log;

/**
 * Hello world!
 *
 */
public class App {

    private static final Log log = new Log("App");
    private static String dbUrl =
        "jdbc:h2:file:./data/productionDatabase;CIPHER=AES";

    public static void main(String[] args) {}
}
