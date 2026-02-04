package com.demo.Utils;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class DatabaseSeeder {

    private static final boolean FORCE_RESEED = true; // <-- metti true una volta, poi torna false

    private DatabaseSeeder() {}

    public static void seedIfEmpty() throws SQLException {
        try (Connection conn = DbConnection.getConnection()) {

            if (FORCE_RESEED) {
                clearAll(conn);
            } else {
                // Se c'è almeno 1 utente, consideriamo il DB già popolato
                if (tableHasAnyRow(conn, "UTENTE")) {
                    System.out.println("DB già popolato, skip.");
                    return;
                }
            }

            System.out.println("Popolo il DB con dati di test...");

            // ---------- UTENTI ----------
            UUID u1 = UUID.randomUUID();
            UUID u2 = UUID.randomUUID();
            UUID u3 = UUID.randomUUID();
            UUID u4 = UUID.randomUUID();
            UUID u5 = UUID.randomUUID();
            UUID u6 = UUID.randomUUID();

            insertUtente(conn, u1, "Mario", "Rossi",  "mario@t.com",  "Via Roma 1",     "pw");
            insertUtente(conn, u2, "Giulia","Bianchi","giulia@t.com", "Via Milano 10",  "pw");
            insertUtente(conn, u3, "Luca",  "Verdi",  "luca@t.com",   "Via Napoli 22",  "pw");
            insertUtente(conn, u4, "Anna",  "Neri",   "anna@t.com",   "Via Firenze 5",  "pw");
            insertUtente(conn, u5, "Paolo", "Gialli", "paolo@t.com",  "Via Torino 8",   "pw");
            insertUtente(conn, u6, "Elena", "Blu",    "elena@t.com",  "Via Genova 15",  "pw");

            // ---------- HOST ----------
            insertHost(conn, u1, "H-1001", true);
            insertHost(conn, u2, "H-1002", false);
            insertHost(conn, u3, "H-1003", true);

            // ---------- ABITAZIONI ----------
            UUID a1 = UUID.randomUUID();
            UUID a2 = UUID.randomUUID();
            UUID a3 = UUID.randomUUID();
            UUID a4 = UUID.randomUUID();

            insertAbitazione(conn, a1, "Casa Centro", "Roma - Via A 1",  2, 3, 1, 120.00, u1);
            insertAbitazione(conn, a2, "Loft Mare",   "Napoli - Via B 2",1, 2, 2, 90.00,  u1);
            insertAbitazione(conn, a3, "Baita",       "Trento - Via C 3",3, 4, 0, 150.00, u2);
            insertAbitazione(conn, a4, "Attico",      "Milano - Via D 4",2, 2, 5, 200.00, u3);

            // ---------- PRENOTAZIONI (ultimi 30 giorni) ----------
            LocalDate today = LocalDate.now();

            insertPrenotazione(conn, UUID.randomUUID(), "Pren A1", "Roma - Via A 1", 2,
                    today.minusDays(25), today.minusDays(18), u4, a1);
            insertPrenotazione(conn, UUID.randomUUID(), "Pren A2", "Roma - Via A 1", 2,
                    today.minusDays(17), today.minusDays(12), u5, a1);
            insertPrenotazione(conn, UUID.randomUUID(), "Pren A3", "Roma - Via A 1", 2,
                    today.minusDays(11), today.minusDays(5),  u6, a1);

            insertPrenotazione(conn, UUID.randomUUID(), "Pren B1", "Napoli - Via B 2", 1,
                    today.minusDays(20), today.minusDays(13), u5, a2);

            insertPrenotazione(conn, UUID.randomUUID(), "Pren D1", "Milano - Via D 4", 2,
                    today.minusDays(4), today.plusDays(3), u4, a4);

            System.out.println("[SEED] Seed completato ✅");
        }
    }

    private static void clearAll(Connection conn) throws SQLException {
        System.out.println("Pulizia DB (FORCE_RESEED=true)...");
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM PRENOTAZIONE");
            st.executeUpdate("DELETE FROM ABITAZIONE");
            st.executeUpdate("DELETE FROM HOST");
            st.executeUpdate("DELETE FROM UTENTE");
        }
    }

    private static boolean tableHasAnyRow(Connection conn, String table) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM " + table + " LIMIT 1");
             ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    }

    private static void insertUtente(Connection conn, UUID id, String nome, String cognome, String email, String indirizzo, String password) throws SQLException {
        String sql = "INSERT INTO UTENTE (id, nome, cognome, email, indirizzo, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.setString(2, nome);
            ps.setString(3, cognome);
            ps.setString(4, email);
            ps.setString(5, indirizzo);
            ps.setString(6, password);
            ps.executeUpdate();
        }
    }

    private static void insertHost(Connection conn, UUID id, String codiceHost, boolean isSuperhost) throws SQLException {
        String sql = "INSERT INTO HOST (id, codice_host, is_superhost) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.setString(2, codiceHost);
            ps.setBoolean(3, isSuperhost);
            ps.executeUpdate();
        }
    }

    private static void insertAbitazione(Connection conn, UUID id, String name, String indirizzo,
                                         int numLocali, Integer numPostiLetto, Integer piano, Double prezzo, UUID hostId) throws SQLException {
        String sql = "INSERT INTO ABITAZIONE (id, name, indirizzo, num_locali, num_posti_letto, piano, prezzo, host_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.setString(2, name);
            ps.setString(3, indirizzo);
            ps.setInt(4, numLocali);
            if (numPostiLetto == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, numPostiLetto);
            if (piano == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, piano);
            if (prezzo == null) ps.setNull(7, Types.NUMERIC); else ps.setDouble(7, prezzo);
            ps.setObject(8, hostId);
            ps.executeUpdate();
        }
    }

    private static void insertPrenotazione(Connection conn, UUID id, String name, String address, int numLocali,
                                           LocalDate dataInizio, LocalDate dataFine, UUID utenteId, UUID abitazioneId) throws SQLException {
        String sql = "INSERT INTO PRENOTAZIONE (id, name, address, num_locali, data_inizio, data_fine, utente_id, abitazione_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setInt(4, numLocali);
            ps.setObject(5, dataInizio);
            ps.setObject(6, dataFine);
            ps.setObject(7, utenteId);
            ps.setObject(8, abitazioneId);
            ps.executeUpdate();
        }
    }
}
