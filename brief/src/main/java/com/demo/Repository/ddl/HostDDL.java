package com.demo.Repository.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class HostDDL {

    private HostDDL() {
    }

    public static void createTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS HOST (" +
                "id UUID PRIMARY KEY REFERENCES UTENTE(id) ON DELETE CASCADE, " +
                "codice_host VARCHAR(50) NOT NULL, " +
                "is_superhost BOOLEAN NOT NULL DEFAULT FALSE" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}
