package com.example.database;

import static com.example.util.Log.clear;
import static com.example.util.Log.start;
import static com.example.util.Log.stop;

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
        start("destroy db");
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
        stop("destroy db");
    }

    public static void newUser(String password, String key)
        throws SQLException {
        start("new user");
        destroyDb();
        login(password);
        createTables();
        newApiKey("massive", key);
        stop("new user");
    }

    public static void login(String password) throws SQLException {
        start("login");
        connection = DriverManager.getConnection(DB_URL, USER, password);
        stop("login");
    }

    public static boolean dbExists() {
        start("check db exists");
        for (val db : DB_EXTENSIONS) {
            val file = new File(DB_PATH + db);
            if (file.exists()) {
                stop("check db exists");
                return true;
            }
        }
        stop("check db exists");
        return false;
    }

    public static void newApiKey(String name, String key) throws SQLException {
        start("new api key");
        insert(Table.API, new String[] { name, key });
        stop("new api key");
    }

    public static String getApiKey(String name) throws SQLException {
        start("get api key");
        String result = String.valueOf(selectOne(Table.API, name));
        stop("get api key");
        return result;
    }

    private static enum Table {
        INVENTORY,
        TRANSACTIONS,
        API,
    }

    private static void createTables() throws SQLException {
        start("create tables");
        String sql = Query.createTables();
        val statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
        connection.commit();
        stop("create tables");
    }

    private static void insert(Table table, Object[] values)
        throws SQLException {
        start("insert row");
        String query = switch (table) {
            case TRANSACTIONS -> Query.insetIntoTrans(values);
            case API -> Query.insertIntoApi(values);
            case INVENTORY -> Query.insetIntoTrans(values);
        };

        val statement = connection.createStatement();
        statement.execute(query);

        statement.close();
        connection.commit();
        stop("insert row");
    }

    private static Object selectOne(Table table, String target)
        throws SQLException {
        start("select record");
        String sql = switch (table) {
            case API -> Query.selectFromApi(target);
            default -> "";
        };

        val statement = connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            stop("select record");
            return results.getObject(1);
        } else {
            clear("select failed");
            throw new SQLException("no objects found in table");
        }
    }

    private static Object[] selectAll(Table table) throws SQLException {
        start("select all");
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
            results.add(String.valueOf(rowString));
        }

        statement.close();

        stop("select all");
        return results.toArray(new String[0]);
    }
}
