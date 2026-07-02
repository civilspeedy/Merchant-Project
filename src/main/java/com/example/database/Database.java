package com.example.database;

import com.example.database.records.InputRecord;
import com.example.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

    private static final Log log = new Log("Database");
    private static final String USER = "sa";
    private static Connection connection;
    private static boolean tablesCreated = false;
    private static final String CREATE_TABLES_PATH = "sql/createTables.sql";
    private static final String INSERT_USER_PATH = "sql/insertIntoUser.sql";
    private static final String INSERT_INVENT_PATH = "sql/insertIntoInvent.sql";
    private static final String INSERT_TRANS_PATH = "sql/insertIntoTrans.sql";

    public static enum Table {
        USER,
        INVENTORY,
        TRANSACTIONS,
    }

    private static final String getQuery(String path) throws IOException {
        InputStream stream = Database.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IOException(path + " could not be found");
        }
        return new String(stream.readAllBytes());
    }

    private static final void createTables() throws Exception {
        String sql = getQuery(CREATE_TABLES_PATH);
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
        connection.commit();
        tablesCreated = true;
    }

    public static final void insert(Table table, InputRecord data)
        throws Exception {
        if (!tablesCreated) {
            throw new IllegalStateException(
                "tables may not exist! createTables() must run before this method!"
            );
        }

        String path = switch (table) {
            case USER -> INSERT_USER_PATH;
            case INVENTORY -> INSERT_INVENT_PATH;
            case TRANSACTIONS -> INSERT_TRANS_PATH;
        };

        String sql = getQuery(path);

        sql = sql.replaceFirst(
            data.getReplacementString(),
            data.getFieldString()
        );

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
        connection.commit();
    }

    public static final void connect(String url) throws Exception {
        String password = System.getenv("DB_PASSWORD").trim();

        if (password == null) {
            throw new IllegalArgumentException("DB_PASSWORD not defined");
        }

        connection = DriverManager.getConnection(url, USER, password);
    }

    public static final void close() throws SQLException {
        log.out("closing database");
        connection.commit();
        connection.close();
    }
}
