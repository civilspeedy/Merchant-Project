package com.example.database;

import com.example.database.records.InputRecord;
import com.example.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

    public static enum Insert {
        USER("insertIntoUser.sql"),
        TRANS("insertIntoTrans.sql"),
        INVENT("insertIntoInvent.sql");

        public final String path;

        private Insert(String path) {
            this.path = path;
        }
    }

    public static final void insert(Insert queryType, InputRecord data)
        throws Exception {
        if (!tablesCreated) {
            throw new IllegalStateException(
                "tables may not exist! createTables() must run before this method!"
            );
        }

        String sql = getQuery("/sql/insert/" + queryType.path);

        PreparedStatement statement = connection.prepareStatement(sql);
        String[] fields = data.getFieldArray();

        for (int i = 0; i < fields.length; i++) {
            statement.setString(i + 1, fields[i]);
        }

        statement.execute();

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
        PASSWORD("password", "users/selectPassword.sql"),
        ALL_INVENT("*", "inventory/selectAllInventory.sql"),
        ALL_TRANS("*", "transactions/selectAllTransactions.sql");

        public final String label;
        public final String path;

        private Select(String label, String path) {
            this.label = label;
            this.path = path;
        }
    }

    public static final String[] select(Select selection, String target)
        throws Exception {
        String sql = getQuery("/sql/select/" + selection.path);
        PreparedStatement statement = connection.prepareStatement(sql);
        if (target != null) {
            statement.setString(1, target);
        }

        ResultSet result = statement.executeQuery();

        ArrayList<String> results = new ArrayList<String>();
        if (selection.label.equals("*")) {
            while (result.next()) {
                StringBuilder rowString = new StringBuilder();
                ResultSetMetaData meta = result.getMetaData();

                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    if (i > 1) {
                        rowString.append(',');
                    }
                    rowString.append(result.getString(i));
                }
                results.add(rowString.toString());
            }
        } else {
            while (result.next()) {
                results.add(result.getString(selection.label));
            }
        }

        statement.close();

        return results.toArray(new String[0]);
    }
}
