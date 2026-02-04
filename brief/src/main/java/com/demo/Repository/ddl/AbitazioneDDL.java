package com.demo.Repository.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class AbitazioneDDL {

    private AbitazioneDDL() {
    }

    public static void createTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ABITAZIONE (" +
                "id UUID PRIMARY KEY, " +
                "name VARCHAR(150) NOT NULL, " +
                "indirizzo VARCHAR(255) NOT NULL, " +
                "num_locali INTEGER NOT NULL, " +
                "num_posti_letto INTEGER, " +
                "piano INTEGER, " +
                "prezzo NUMERIC(10,2), " +
                "data_inizio DATE, " +
                "data_fine DATE, " +
                "host_id UUID NOT NULL REFERENCES HOST(id) ON DELETE CASCADE" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
}
