package com.demo.Repository.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.demo.Utils.DbConnection;

public class HostRepository {

    public void insertHost(UUID utenteId, String codiceHost, boolean isSuperhost) throws SQLException {
        String sql = "INSERT INTO HOST (id, codice_host, is_superhost) VALUES (?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, utenteId);
            stmt.setString(2, codiceHost);
            stmt.setBoolean(3, isSuperhost);

            stmt.executeUpdate();
        }
    }

    public boolean updateHost(UUID utenteId, String codiceHost, boolean isSuperhost) throws SQLException {
        String sql = "UPDATE HOST SET codice_host = ?, is_superhost = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codiceHost);
            stmt.setBoolean(2, isSuperhost);
            stmt.setObject(3, utenteId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteHost(UUID utenteId) throws SQLException {
        String sql = "DELETE FROM HOST WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, utenteId);
            return stmt.executeUpdate() > 0;
        }
    }
}
