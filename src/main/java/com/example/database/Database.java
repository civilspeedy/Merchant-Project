package com.example.database;

import com.example.database.records.InputRecord;
import com.example.database.records.Inventory;
import com.example.database.records.Transaction;
import com.example.database.records.User;
import com.example.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {

    private static final Log log = new Log("Database");
    private static final String USER = "sa";
    private static Connection connection;
    private static boolean tablesCreated = false;
    private static boolean dbConnected = false;
    private static final String DROP_ALL_OBJECTS = "DROP ALL OBJECTS;";

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

    public static final void createTables() throws Exception {
        if (!dbConnected) {
            throw new IllegalStateException(
                "database has not been connected yet!"
            );
        }
        String sql = getQuery("/sql/create/createTables.sql");
        String[] queries = sql.split(";");
        Statement statement = connection.createStatement();

        for (String query : queries) {
            statement.addBatch(query + ";");
        }
        statement.executeBatch();
        statement.close();
        connection.commit();
        tablesCreated = true;
    }

    public static final void insert(InputRecord data) throws Exception {
        if (!tablesCreated) {
            throw new IllegalStateException(
                "tables may not exist! createTables() must run before this method!"
            );
        }

        String path = "/sql/insert/";
        if (data instanceof User) {
            path.concat("insertIntoUser.sql");
        } else if (data instanceof Transaction) {
            path.concat("insertIntoTrans.sql");
        } else if (data instanceof Inventory) {
            path.concat("insertIntoInvent.sql");
        } else {
            throw new IllegalArgumentException("invalid record type");
        }
        String sql = getQuery(path);

        // replace with statement.setString()

        System.out.println(sql);

        PreparedStatement statement = connection.prepareStatement(sql);
        String[] fields = data.getFieldArray();

        for (int i = 0; i < fields.length; i++) {
            statement.setString(i + 1, fields[i]);
        }

        statement.close();
        connection.commit();
    }

    public static final void connect(String url, String password)
        throws SQLException {
        connection = DriverManager.getConnection(url, USER, password);
        dbConnected = true;
    }

    public static final void close() throws SQLException {
        log.out("closing database");
        connection.commit();
        connection.close();
    }

    public static final void dropAll() throws SQLException {
        log.out("dropping all tables");
        Statement statement = connection.createStatement();
        statement.execute(DROP_ALL_OBJECTS);
        statement.close();
        connection.commit();
    }

    public static enum Select {
        PASSWORD,
        USER_ID,
        USERNAME,
        ALL_INVENT,
        ALL_TRANS,
    }

    public static final Object select(Select valueToSelect, String where)
        throws SQLException {
        String query = "/sql/select/";

        query += switch (valueToSelect) {
            case PASSWORD -> "users/selectUserPassword.sql";
            case USER_ID -> "users/selectUserId.sql";
            case USERNAME -> "users/selectUserName.sql";
            case ALL_INVENT -> "inventory/selectAllInventory.sql";
            case ALL_TRANS -> "transactions/selectAllTransactions.sql";
        };

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, where);
        ResultSet results = statement.executeQuery();
        return results; // need to figure out value selection
    }
}
