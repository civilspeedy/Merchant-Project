package com.example.database;

import java.io.File;
import java.sql.SQLException;

public class Manager {

    private static Database database;
    private static final String DB_PATH = "./data/database";
    private static final String DB_URL = "jdbc:h2:file:" + DB_PATH;
    private static final String[] DB_EXTENSIONS = new String[] {
        ".mv.db",
        ".trace.db",
        ".lock.db",
    };

    public static void newUser(String password) throws SQLException {
        for (var db : DB_EXTENSIONS) {
            var path = DB_PATH + db;
            var file = new File(path);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException(
                        "Failed to delete database file: " + path
                    );
                }
            }
        }

        database = new Database(DB_URL, password);
    }

    public static void login(String password) throws SQLException {
        database = new Database(DB_URL, password);
    }

    public static void newApiKey(String key) {}
}
