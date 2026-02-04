package com.demo.Repository.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class UtenteDDL {

    private UtenteDDL() {
    }

    public static void createTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS UTENTE (" +
                "id UUID PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "cognome VARCHAR(100) NOT NULL, " +
                "email VARCHAR(25) NOT NULL UNIQUE, " +
                "indirizzo VARCHAR(255) NOT NULL, " +
                "password VARCHAR(25)" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}
