package it.biblioteca.dao;

import it.biblioteca.db.DatabaseManager;
import it.biblioteca.model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UtenteDAO {

    private Connection getConn() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    public void inserisci(Utente utente) throws SQLException {
        String sql = "INSERT INTO utenti (nome, email) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    System.out.println("[DAO] Utente inserito con ID: " + keys.getInt(1));
            }
        }
    }

    public List<Utente> trovaTutti() throws SQLException {
        List<Utente> lista = new ArrayList<>();
        String sql = "SELECT * FROM utenti ORDER BY id";
        try (Statement stmt = getConn().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mappaRiga(rs));
        }
        return lista;
    }

    public Optional<Utente> trovaPerId(int id) throws SQLException {
        String sql = "SELECT * FROM utenti WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mappaRiga(rs));
            }
        }
        return Optional.empty();
    }

    private Utente mappaRiga(ResultSet rs) throws SQLException {
        return new Utente(
            rs.getInt   ("id"),
            rs.getString("nome"),
            rs.getString("email")
        );
    }
}
