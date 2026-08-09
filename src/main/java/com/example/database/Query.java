package com.example.database;

import lombok.val;

final class Query {

    private static final String API_TABLE_NAME = "api_keys";
    private static final String API_FIELDS = "(name, api_key)";

    private static final String TRANS_TABLE_NAME = "transactions";
    private static final String TRANS_FIELDS =
        "(code, exchange, quantity, price, buy)";

    private static final String INVENT_TABLE_NAME = "inventory";
    private static final String INVENT_FIELDS = "(code, exchange, quantity)";

    private static final String INSERT = "INSERT INTO %s %s VALUES %s;";
    private static final String SELECT = "SELECT %s FROM %s WHERE %s = '%s';";
    private static final String SELECT_ALL = "SELECT * FROM %s;";

    private static final String intoBrackets(Object[] values) {
        val builder = new StringBuilder().append('(');
        for (int i = 0; i < values.length; i++) {
            builder.append('\'').append(String.valueOf(values[i])).append('\'');
            if (i != values.length - 1) builder.append(',');
        }
        return builder.append(')').toString();
    }

    public static final String[] createTables() {
        val createInventTable = String.format(
            """
            CREATE TABLE IF NOT EXISTS %s (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code NVARCHAR(5) NOT NULL,
                exchange NVARCHAR(12) NOT NULL,
                quantity DOUBLE NOT NULL,
                UNIQUE(code)
            );
            """,
            INVENT_TABLE_NAME
        );

        val createTransTable = String.format(
            """
            CREATE TABLE IF NOT EXISTS %s (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code NVARCHAR(5) NOT NULL,
                exchange NVARCHAR(12) NOT NULL,
                quantity DOUBLE NOT NULL,
                price DOUBLE NOT NULL,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                buy BOOLEAN NOT NULL
            );
            """,
            TRANS_TABLE_NAME
        );

        val createApiTable = String.format(
            """
                CREATE TABLE IF NOT EXISTS %s (
                    name NVARCHAR(12) NOT NULL PRIMARY KEY,
                    api_key NVARCHAR(25) NOT NULL
                );
            """,
            API_TABLE_NAME
        );

        return new String[] {
            createInventTable,
            createTransTable,
            createApiTable,
        };
    }

    public static final String insertIntoApi(Object[] values) {
        return String.format(
            INSERT,
            API_TABLE_NAME,
            API_FIELDS,
            intoBrackets(values)
        );
    }

    public static final String selectFromApi(String name) {
        return String.format(SELECT, "api_key", API_TABLE_NAME, "name", name);
    }

    public static final String insertIntoInvent(Object[] values) {
        return String.format(
            INSERT,
            INVENT_TABLE_NAME,
            INVENT_FIELDS,
            intoBrackets(values)
        );
    }

    public static final String selectAllFromInvent() {
        return String.format(SELECT_ALL, INVENT_TABLE_NAME);
    }

    public static final String insetIntoTrans(Object[] values) {
        return String.format(
            INSERT,
            TRANS_TABLE_NAME,
            TRANS_FIELDS,
            intoBrackets(values)
        );
    }

    public static final String selectAllFromTrans() {
        return String.format(SELECT_ALL, TRANS_TABLE_NAME);
    }
}
