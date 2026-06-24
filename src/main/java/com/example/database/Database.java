package com.example.database;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import tools.jackson.databind.ObjectMapper;

public final class Database {

    private static final int PORT = 1433;
    private static final String DOMAIN = "localhost";
    private static final String NAME = "sql1";
    private static final boolean ENCRYPTED = true;
    private static final boolean TRUST_CERT = true;
    private static final String user = "sa";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String getServerUrl() {
        return new StringBuilder("jdbc:sqlserver://")
            .append(DOMAIN)
            .append(PORT)
            .append(':')
            .append(";databaseName=")
            .append(NAME)
            .append(";encrypted=")
            .append(ENCRYPTED)
            .append(";trustServerCertificate=")
            .append(TRUST_CERT)
            .append(';')
            .toString();
    }

    private static final String getPassword() throws Exception {
        URL file = Database.class
            .getClassLoader()
            .getResource(".secrets/mysqlpass.txt");
        if (file == null) {
            throw new IllegalArgumentException("unable to find secret!");
        }

        return Files.readString(Paths.get(file.toURI()));
    }
}
