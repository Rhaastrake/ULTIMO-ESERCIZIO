package it.biblioteca.dao;

import it.biblioteca.db.DatabaseManager;
import it.biblioteca.model.Libro;
import it.biblioteca.model.Prestito;
import it.biblioteca.model.Utente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class PrestitoDAO {

    private Connection getConn() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    public void inserisci(Prestito prestito) throws SQLException {
        // Prima aggiorna la disponibilita del libro
        String sqlLibro = "UPDATE libri SET disponibile = FALSE WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sqlLibro)) {
            ps.setInt(1, prestito.getLibro().getId());
            ps.executeUpdate();
        }

        // Poi inserisce il prestito
        String sql = "INSERT INTO prestiti (utente_id, libro_id, data_inizio, data_fine) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt (1, prestito.getUtente().getId());
            ps.setInt (2, prestito.getLibro().getId());
            ps.setDate(3, Date.valueOf(prestito.getDataInizio()));
            ps.setDate(4, Date.valueOf(prestito.getDataFine()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    System.out.println("[DAO] Prestito inserito con ID: " + keys.getInt(1));
            }
        }
    }

    /**
     * Recupera tutti i prestiti con un JOIN su utenti e libri.
     * Una sola query restituisce tutto il necessario.
     */
    public List<Prestito> trovaTutti() throws SQLException {
        List<Prestito> lista = new ArrayList<>();
        String sql = """
            SELECT p.id, p.data_inizio, p.data_fine,
                   u.id AS uid, u.nome, u.email,
                   l.id AS lid, l.titolo, l.autore, l.anno, l.disponibile
            FROM   prestiti p
            JOIN   utenti u ON p.utente_id = u.id
            JOIN   libri  l ON p.libro_id  = l.id
            ORDER  BY p.id
        """;
        try (Statement stmt = getConn().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mappaRiga(rs));
        }
        return lista;
    }

    /** Restituisce i prestiti scaduti (data_fine < oggi) */
    public List<Prestito> trovaScaduti() throws SQLException {
        List<Prestito> lista = new ArrayList<>();
        String sql = """
            SELECT p.id, p.data_inizio, p.data_fine,
                   u.id AS uid, u.nome, u.email,
                   l.id AS lid, l.titolo, l.autore, l.anno, l.disponibile
            FROM   prestiti p
            JOIN   utenti u ON p.utente_id = u.id
            JOIN   libri  l ON p.libro_id  = l.id
            WHERE  p.data_fine < CURRENT_DATE
            ORDER  BY p.data_fine
        """;
        try (Statement stmt = getConn().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mappaRiga(rs));
        }
        return lista;
    }

    private Prestito mappaRiga(ResultSet rs) throws SQLException {
        Utente utente = new Utente(
            rs.getInt   ("uid"),
            rs.getString("nome"),
            rs.getString("email")
        );
        Libro libro = new Libro(
            rs.getInt   ("lid"),
            rs.getString("titolo"),
            rs.getString("autore"),
            rs.getInt   ("anno")
        );
        libro.setDisponibile(rs.getBoolean("disponibile"));

        LocalDate inizio = rs.getDate("data_inizio").toLocalDate();
        return new Prestito(rs.getInt("id"), utente, libro, inizio);
    }
}
