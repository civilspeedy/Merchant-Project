package com.example.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import lombok.val;

public final class Database {

    private static final String DB_PATH = "./data/database";
    private static final String DB_URL = "jdbc:h2:file:" + DB_PATH;
    private static final String[] DB_EXTENSIONS = new String[] {
        ".mv.db",
        ".trace.db",
        ".lock.db",
    };

    private static final String USER = "sa";
    private static Connection connection;

    public static void destroyDb() {
        for (val db : DB_EXTENSIONS) {
            val path = DB_PATH + db;
            val file = new File(path);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException(
                        "Failed to delete database file: " + path
                    );
                }
            }
        }
    }

    public static void newUser(String password, String key)
        throws SQLException {
        destroyDb();
        login(password);
        createTables();
        newApiKey("massive", key);
    }

    public static void login(String password) throws SQLException {
        connection = DriverManager.getConnection(DB_URL, USER, password);
    }

    public static boolean dbExists() {
        for (val db : DB_EXTENSIONS) {
            val file = new File(DB_PATH + db);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    public static void newApiKey(String name, String key) throws SQLException {
        insert(Table.API, new String[] { name, key });
    }

    public static String getApiKey(String name) throws SQLException {
        return String.valueOf(selectOne(Table.API, name));
    }

    private static enum Table {
        INVENTORY,
        TRANSACTIONS,
        API,
    }

    private static void createTables() throws SQLException {
        String[] queries = Query.createTables();
        val statement = connection.createStatement();

        for (var query : queries) {
            statement.addBatch(query);
        }
        statement.executeBatch();
        statement.close();
        connection.commit();
    }

    private static void insert(Table table, Object[] values)
        throws SQLException {
        String query = switch (table) {
            case TRANSACTIONS -> Query.insetIntoTrans(values);
            case API -> Query.insertIntoApi(values);
            case INVENTORY -> Query.insetIntoTrans(values);
        };

        val statement = connection.createStatement();
        statement.execute(query);

        statement.close();
        connection.commit();
    }

    private static Object selectOne(Table table, String target)
        throws SQLException {
        String sql = switch (table) {
            case API -> Query.selectFromApi(target);
            default -> "";
        };

        val statement = connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            return results.getObject(1);
        } else {
            throw new SQLException("no objects found in table");
        }
    }

    private static Object[] selectAll(Table table) throws SQLException {
        String sql;

        if (table == Table.TRANSACTIONS) {
            sql = Query.selectAllFromTrans();
        } else if (table == Table.INVENTORY) {
            sql = Query.selectAllFromInvent();
        } else {
            throw new IllegalArgumentException("Invalid table for select all");
        }

        val statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();

        val results = new ArrayList<String>();

        while (result.next()) {
            val rowString = new StringBuilder();
            val meta = result.getMetaData();

            for (int i = 1; i <= meta.getColumnCount(); i++) {
                if (i > 1) {
                    rowString.append(',');
                }
                rowString.append(result.getObject(i));
            }
            results.add(rowString.toString());
        }

        statement.close();

        return results.toArray(new String[0]);
    }
}
