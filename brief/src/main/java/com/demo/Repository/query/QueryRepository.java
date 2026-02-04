package com.demo.Repository.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.demo.Utils.DbConnection;

public class QueryRepository {

    public List<Map<String, Object>> getAbitazioniByCodiceHost(String codiceHost) throws SQLException {
        String sql = "SELECT a.* " +
                     "FROM ABITAZIONE a " +
                     "JOIN HOST h ON a.host_id = h.id " +
                     "WHERE h.codice_host = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codiceHost);
            try (ResultSet rs = stmt.executeQuery()) {
                return toList(rs);
            }
        }
    }

    public List<Map<String, Object>> getAllUtenti() throws SQLException {
        String sql = "SELECT * FROM UTENTE";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return toList(rs);
        }
    }

    public List<Map<String, Object>> getAllHost() throws SQLException {
        String sql = "SELECT h.id, h.codice_host, h.is_superhost, u.nome, u.cognome, u.email, u.indirizzo " +
                     "FROM HOST h " +
                     "JOIN UTENTE u ON u.id = h.id";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return toList(rs);
        }
    }

    public Map<String, Object> getUltimaPrenotazioneByUtenteId(UUID utenteId) throws SQLException {
        String sql = "SELECT p.* " +
                     "FROM PRENOTAZIONE p " +
                     "WHERE p.utente_id = ? " +
                     "ORDER BY p.data_fine DESC " +
                     "LIMIT 1";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, utenteId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Map<String, Object>> rows = toList(rs);
                return rows.isEmpty() ? null : rows.get(0);
            }
        }
    }

    public Map<String, Object> getAbitazionePiuGettonataUltimoMese() throws SQLException {
        String sql = "SELECT a.*, COUNT(p.id) AS prenotazioni_count " +
                     "FROM PRENOTAZIONE p " +
                     "JOIN ABITAZIONE a ON p.abitazione_id = a.id " +
                     "WHERE p.data_inizio >= CURRENT_DATE - INTERVAL '1 month' " +
                     "GROUP BY a.id " +
                     "ORDER BY prenotazioni_count DESC " +
                     "LIMIT 1";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Map<String, Object>> rows = toList(rs);
            return rows.isEmpty() ? null : rows.get(0);
        }
    }

    public List<Map<String, Object>> getHostConPiuPrenotazioniUltimoMese() throws SQLException {
        String sql = "SELECT h.id, h.codice_host, h.is_superhost, u.nome, u.cognome, u.email, " +
                     "COUNT(p.id) AS prenotazioni_count " +
                     "FROM HOST h " +
                     "JOIN UTENTE u ON u.id = h.id " +
                     "JOIN ABITAZIONE a ON a.host_id = h.id " +
                     "JOIN PRENOTAZIONE p ON p.abitazione_id = a.id " +
                     "WHERE p.data_inizio >= CURRENT_DATE - INTERVAL '1 month' " +
                     "GROUP BY h.id, h.codice_host, h.is_superhost, u.nome, u.cognome, u.email " +
                     "ORDER BY prenotazioni_count DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return toList(rs);
        }
    }

    public List<Map<String, Object>> getAllSuperHost() throws SQLException {
        String sql = "SELECT h.id, h.codice_host, h.is_superhost, u.nome, u.cognome, u.email, u.indirizzo " +
                     "FROM HOST h " +
                     "JOIN UTENTE u ON u.id = h.id " +
                     "WHERE h.is_superhost = TRUE";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return toList(rs);
        }
    }

    public List<Map<String, Object>> getTop5UtentiPiuGiorniUltimoMese() throws SQLException {
        String sql = "SELECT u.id, u.nome, u.cognome, u.email, u.indirizzo, " +
                     "SUM((p.data_fine - p.data_inizio) + 1) AS giorni_prenotati " +
                     "FROM PRENOTAZIONE p " +
                     "JOIN UTENTE u ON u.id = p.utente_id " +
                    //  "WHERE p.data_inizio >= CURRENT_DATE - INTERVAL '1 month' " +
                     "GROUP BY u.id, u.nome, u.cognome, u.email, u.indirizzo " +
                     "ORDER BY giorni_prenotati DESC " +
                     "LIMIT 5";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return toList(rs);
        }
    }

    public Double getNumeroMedioPostiLetto() throws SQLException {
        String sql = "SELECT AVG(num_posti_letto) AS media_posti_letto FROM ABITAZIONE";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("media_posti_letto");
            }
            return null;
        }
    }
    public long countUtenti() throws SQLException {
    String sql = "SELECT COUNT(*) AS total FROM UTENTE";
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        rs.next();
        return rs.getLong("total");
    }
}

public long countHost() throws SQLException {
    String sql = "SELECT COUNT(*) AS total FROM HOST";
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        rs.next();
        return rs.getLong("total");
    }
}

public long countSuperHost() throws SQLException {
    String sql = "SELECT COUNT(*) AS total FROM HOST WHERE is_superhost = TRUE";
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        rs.next();
        return rs.getLong("total");
    }
}


    private List<Map<String, Object>> toList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }
}
