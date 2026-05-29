package it.biblioteca.dao;

import it.biblioteca.db.DatabaseManager;
import it.biblioteca.model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class LibroDAO {

    private Connection getConn() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    // ── CREATE
    public void inserisci(Libro libro) throws SQLException {
        String sql = "INSERT INTO libri (titolo, autore, anno, disponibile) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, libro.getTitolo());
            ps.setString(2, libro.getAutore());
            ps.setInt   (3, libro.getAnno());
            ps.setBoolean(4, libro.isDisponibile());
            ps.executeUpdate();

            // Recupera l'ID generato dal DB (AUTO_INCREMENT)
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("[DAO] Libro inserito con ID: " + keys.getInt(1));
                }
            }
        }
    }

    // ── READ ALL
    public List<Libro> trovaTutti() throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libri ORDER BY id";
        try (Statement stmt = getConn().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mappaRiga(rs));
            }
        }
        return lista;
    }

    // ── READ BY ID
    public Optional<Libro> trovaPerId(int id) throws SQLException {
        String sql = "SELECT * FROM libri WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mappaRiga(rs));
            }
        }
        return Optional.empty();
    }

    // ── READ BY AUTORE
    public List<Libro> trovaPerAutore(String autore) throws SQLException {
        List<Libro> lista = new ArrayList<>();
        // LIKE con % per ricerca parziale, LOWER per case-insensitive
        String sql = "SELECT * FROM libri WHERE LOWER(autore) LIKE LOWER(?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, "%" + autore + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mappaRiga(rs));
            }
        }
        return lista;
    }

    // ── READ DISPONIBILI
    public List<Libro> trovaDisponibili() throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libri WHERE disponibile = TRUE ORDER BY titolo";
        try (Statement stmt = getConn().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mappaRiga(rs));
        }
        return lista;
    }

    // ── DELETE
    public void elimina(int id) throws SQLException {
        String sql = "DELETE FROM libri WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Mappa una riga del ResultSet in un oggetto Libro.
     * Metodo privato di utilita: centralizza la mappatura in un solo posto.
     */
    private Libro mappaRiga(ResultSet rs) throws SQLException {
        Libro libro = new Libro(
            rs.getInt    ("id"),
            rs.getString ("titolo"),
            rs.getString ("autore"),
            rs.getInt    ("anno")
        );
        libro.setDisponibile(rs.getBoolean("disponibile"));
        return libro;
    }
}
