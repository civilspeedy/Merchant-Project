package com.example.database;

import com.example.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Database {

    private static final Log log = new Log("Database");
    private static final String USER = "sa";
    private static Connection connection;
    private static final String DROP_ALL_OBJECTS = "DROP ALL OBJECTS;";

    Database(String url, String password) throws SQLException {
        connection = DriverManager.getConnection(url, USER, password);
        this.createTables();
    }

    public static enum Table {
        INVENTORY,
        TRANSACTIONS,
        API,
    }

    public void createTables() throws SQLException {
        String[] queries = Query.createTables();
        var statement = connection.createStatement();

        for (var query : queries) {
            statement.addBatch(query);
        }
        statement.executeBatch();
        statement.close();
        connection.commit();
    }

    public void insert(Table table, Object[] values) throws SQLException {
        String query = switch (table) {
            case TRANSACTIONS -> Query.insetIntoTrans(values);
            case API -> Query.insertIntoApi(values);
            case INVENTORY -> Query.insetIntoTrans(values);
        };

        var statement = connection.createStatement();
        statement.execute(query);

        statement.close();
        connection.commit();
    }

    public void close() throws SQLException {
        log.out("closing database");
        connection.commit();
        connection.close();
    }

    public void dropAll() throws SQLException {
        log.out("dropping all tables");
        var statement = connection.createStatement();
        statement.execute(DROP_ALL_OBJECTS);
        statement.close();
        connection.commit();
    }

    public Object selectOne(Table table, String target) throws SQLException {
        String sql = switch (table) {
            case API -> Query.selectFromApi(target);
            default -> "";
        };

        var statement = connection.prepareStatement(sql);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            return results.getObject(0);
        } else {
            throw new SQLException("no objects found in table");
        }
    }

    public Object[] selectAll(Table table) throws SQLException {
        String sql = switch (table) {
            case TRANSACTIONS -> Query.selectAllFromTrans();
            case INVENTORY -> Query.selectAllFromInvent();
            default -> "";
        };

        var statement = connection.prepareStatement(sql);

        ResultSet result = statement.executeQuery();

        var results = new ArrayList<String>();

        while (result.next()) {
            var rowString = new StringBuilder();
            var meta = result.getMetaData();

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
