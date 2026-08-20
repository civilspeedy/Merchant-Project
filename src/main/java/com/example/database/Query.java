package com.example.database;

import lombok.val;

/**
 * Class for generating SQL queries. All methods are static.
 */
public final class Query {
    // API table
    private static final String API_TABLE_NAME = "api_keys";
    private static final String API_FIELDS = "(name, api_key)";
    private static final int API_MAX_FIELDS = 2;

    // Transaction Table
    private static final String TRANS_TABLE_NAME = "transactions";
    private static final String TRANS_FIELDS = "(code, exchange, quantity, price, buy)";
    private static final int TRANS_MAX_FIELDS = 5;

    // Inventory Table
    private static final String INVENT_TABLE_NAME = "inventory";
    private static final String INVENT_FIELDS = "(code, exchange, quantity)";
    private static final int INVENT_MAX_FIELDS = 3;

    // Generic Queries
    private static final String INSERT = "INSERT INTO %s %s VALUES %s;";
    private static final String SELECT = "SELECT %s FROM %s WHERE %s = '%s';";
    private static final String SELECT_ALL = "SELECT * FROM %s;";

    /**
     * Error message to be used when an incorrect number of fields is used when
     * querying a table.
     */
    private static final String INVALID_INPUT = "%s requires %s values, no more, no less";

    /**
     * Takes in an array of values and returns a string wrapped in brackets and
     * commas between each value, for use in an SQL query.
     * 
     * @param values Array of values to be used in SQL query.
     * @return String representation of values. E.g. "(val1, val2, val3)"
     */
    private static final String intoBrackets(Object[] values) {
        val builder = new StringBuilder().append('(');
        for (int i = 0; i < values.length; i++) {
            builder.append('\'').append(String.valueOf(values[i])).append('\'');
            if (i != values.length - 1)
                builder.append(',');
        }
        return builder.append(')').toString();
    }

    /**
     * Creates queries to create tables and then concatenates them together to
     * return a bulk query.
     * 
     * @return Single string contain all the table creation queries.
     */
    public static final String createTables() {
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
                INVENT_TABLE_NAME);

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
                TRANS_TABLE_NAME);

        val createApiTable = String.format(
                """
                            CREATE TABLE IF NOT EXISTS %s (
                                name NVARCHAR(12) NOT NULL PRIMARY KEY,
                                api_key NVARCHAR(25) NOT NULL
                            );
                        """,
                API_TABLE_NAME);

        return new StringBuilder(createInventTable)
                .append(createTransTable)
                .append(createApiTable)
                .toString();
    }

    /**
     * Creates a query to insert provided values into the API table.
     * 
     * @param values Values to be inserted into table.
     * @return The query to be executed.
     */
    public static final String insertIntoApi(Object[] values) {
        if (values.length != API_MAX_FIELDS) {
            throw new IllegalArgumentException(
                    String.format(INVALID_INPUT, API_TABLE_NAME, API_MAX_FIELDS));
        }
        return String.format(
                INSERT,
                API_TABLE_NAME,
                API_FIELDS,
                intoBrackets(values));
    }

    /**
     * Creates a query to select a specific value from API table.
     * 
     * @param name The correlated name for the API key being selected.
     * @return The query to be executed.
     */
    public static final String selectFromApi(String name) {
        return String.format(SELECT, "api_key", API_TABLE_NAME, "name", name);
    }

    /**
     * Creates a query to insert values into the Inventory table.
     * 
     * @param values Values to be inserted.
     * @return The query to be executed.
     */
    public static final String insertIntoInvent(Object[] values) {
        if (values.length != INVENT_MAX_FIELDS) {
            throw new IllegalArgumentException(
                    String.format(
                            INVALID_INPUT,
                            INVENT_TABLE_NAME,
                            INVENT_MAX_FIELDS));
        }
        return String.format(
                INSERT,
                INVENT_TABLE_NAME,
                INVENT_FIELDS,
                intoBrackets(values));
    }

    /**
     * Creates a query to select all values in the Inventory table.
     * 
     * @return The query to be executed.
     */
    public static final String selectAllFromInvent() {
        return String.format(SELECT_ALL, INVENT_TABLE_NAME);
    }

    /**
     * Creates a query to insert values to the Transactions table.
     * 
     * @param values The values to be inserted.
     * @return The query to be executed.
     */
    public static final String insetIntoTrans(Object[] values) {
        if (values.length != TRANS_MAX_FIELDS) {
            throw new IllegalArgumentException(
                    String.format(INVALID_INPUT, TRANS_TABLE_NAME, TRANS_MAX_FIELDS));
        }
        return String.format(
                INSERT,
                TRANS_TABLE_NAME,
                TRANS_FIELDS,
                intoBrackets(values));
    }

    /**
     * Creates a query to select all values in the Transactions table.
     * 
     * @return The query to be executed.
     */
    public static final String selectAllFromTrans() {
        return String.format(SELECT_ALL, TRANS_TABLE_NAME);
    }
}
