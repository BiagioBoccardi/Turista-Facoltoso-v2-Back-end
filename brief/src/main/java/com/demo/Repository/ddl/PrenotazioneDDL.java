package com.demo.Repository.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class PrenotazioneDDL {

    private PrenotazioneDDL() {
    }

    public static void createTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS PRENOTAZIONE (" +
                "id UUID PRIMARY KEY, " +
                "name VARCHAR(150) NOT NULL, " +
                "address VARCHAR(255) NOT NULL, " +
                "num_locali INTEGER NOT NULL, " +
                "data_inizio DATE NOT NULL, " +
                "data_fine DATE NOT NULL, " +
                "utente_id UUID NOT NULL REFERENCES UTENTE(id) ON DELETE CASCADE, " +
                "abitazione_id UUID NOT NULL REFERENCES ABITAZIONE(id) ON DELETE CASCADE" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            stmt.execute(
                    "CREATE OR REPLACE FUNCTION prenotazione_no_overlap() " +
                    "RETURNS TRIGGER AS $$ " +
                    "BEGIN " +
                    "IF EXISTS ( " +
                    "SELECT 1 FROM PRENOTAZIONE p " +
                    "WHERE p.abitazione_id = NEW.abitazione_id " +
                    "AND p.id <> NEW.id " +
                    "AND p.data_inizio <= NEW.data_fine " +
                    "AND NEW.data_inizio <= p.data_fine " +
                    ") THEN " +
                    "RAISE EXCEPTION 'Prenotazione sovrapposta per la stessa abitazione'; " +
                    "END IF; " +
                    "RETURN NEW; " +
                    "END; " +
                    "$$ LANGUAGE plpgsql"
            );
            stmt.execute(
                    "DROP TRIGGER IF EXISTS trg_prenotazione_no_overlap ON PRENOTAZIONE"
            );
            stmt.execute(
                    "CREATE TRIGGER trg_prenotazione_no_overlap " +
                    "BEFORE INSERT OR UPDATE ON PRENOTAZIONE " +
                    "FOR EACH ROW EXECUTE FUNCTION prenotazione_no_overlap()"
            );
        }
    }
}
