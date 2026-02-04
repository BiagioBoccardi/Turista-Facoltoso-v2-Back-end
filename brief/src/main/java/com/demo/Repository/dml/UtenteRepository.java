package com.demo.Repository.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.demo.Utils.DbConnection;
import com.demo.model.Utente;

public class UtenteRepository {

    public void insertUtente(Utente utente) throws SQLException {
        String sql = "INSERT INTO UTENTE (id, nome, cognome, email, indirizzo, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, utente.getId());
            stmt.setString(2, utente.getNome());
            stmt.setString(3, utente.getCognome());
            stmt.setString(4, utente.getEmail());
            stmt.setString(5, utente.getIndirizzo());
            stmt.setString(6, utente.getPassword());

            stmt.executeUpdate();
        }
    }

    public boolean updateUtente(Utente utente) throws SQLException {
        String sql = "UPDATE UTENTE SET nome = ?, cognome = ?, email = ?, indirizzo = ?, password = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, utente.getIndirizzo());
            stmt.setString(5, utente.getPassword());
            stmt.setObject(6, utente.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteUtenteById(java.util.UUID utenteId) throws SQLException {
        String sql = "DELETE FROM UTENTE WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, utenteId);
            return stmt.executeUpdate() > 0;
        }
    }
}
