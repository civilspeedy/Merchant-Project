package com.example.database;

import java.sql.SQLException;

public class Manager {

    private static Database database;
    private static final String DB_URL =
        "jdbc:h2:file:./data/productionDatabase;CIPHER=AES";

    public void newUser(String password) throws SQLException {
        // need to delete file or would just dropping tables be better?
        database = new Database(DB_URL, password);
    }
}
