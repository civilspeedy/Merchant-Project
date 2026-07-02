package com.example;

import com.example.util.Log;

/**
 * Hello world!
 *
 */
public class App {

    private static final Log log = new Log("App");
    private static String dbUrl =
        "jdbc:sqlserver://localhost:1433;databaseName=?;encrypted=true;trustServerCertificate=true;";

    public static void main(String[] args) {
        String dbName = System.getenv("DB_NAME").trim();
        if (dbName == null) {
            log.err(new IllegalArgumentException("DB_NAME not define"));
        }
        dbUrl = dbUrl.replace("?", dbName);
    }
}
