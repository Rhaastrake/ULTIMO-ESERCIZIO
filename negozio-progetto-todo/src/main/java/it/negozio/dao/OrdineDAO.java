package it.negozio.dao;

import it.negozio.db.DatabaseManager;
import it.negozio.model.Cliente;
import it.negozio.model.Ordine;
import it.negozio.model.Prodotto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {

    private Connection getConn() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    /**
     * Salva l'ordine e scala le scorte del prodotto in un'unica transazione.
     *
     * Perché una transazione?
     * Se l'INSERT dell'ordine fallisse dopo l'UPDATE delle scorte,
     * il magazzino risulterebbe scalato senza che esista l'ordine corrispondente.
     * Con commit/rollback o entrambe le operazioni vanno a buon fine, o nessuna.
     */
    public void inserisci(Ordine ordine) throws SQLException {
        Connection conn = getConn();
        conn.setAutoCommit(false);
        try {
            // 1. Scala le scorte — la condizione "AND quantita >= ?" evita
            //    di andare sotto zero anche in caso di richieste concorrenti
            String sqlScorte = "UPDATE prodotti SET quantita = quantita - ? WHERE id = ? AND quantita >= ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlScorte)) {
                ps.setInt(1, ordine.getQuantitaOrdinata());
                ps.setInt(2, ordine.getProdotto().getId());
                ps.setInt(3, ordine.getQuantitaOrdinata());
                int righe = ps.executeUpdate();
                if (righe == 0) {
                    conn.rollback();
                    throw new SQLException("Scorte insufficienti per prodotto id=" + ordine.getProdotto().getId());
                }
            }

            // 2. Inserisce l'ordine
            String sqlOrdine = "INSERT INTO ordini (cliente_id, prodotto_id, quantita_ordinata, data_ordine) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlOrdine, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt (1, ordine.getCliente().getId());
                ps.setInt (2, ordine.getProdotto().getId());
                ps.setInt (3, ordine.getQuantitaOrdinata());
                ps.setDate(4, Date.valueOf(ordine.getDataOrdine()));
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next())
                        System.out.println("[DAO] Ordine inserito con ID: " + keys.getInt(1));
                }
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ── READ ALL con JOIN
    public List<Ordine> trovaTutti() throws SQLException {
        List<Ordine> lista = new ArrayList<>();
        String sql = """
            SELECT o.id, o.quantita_ordinata, o.data_ordine,
                   c.id AS cid, c.nome, c.email,
                   p.id AS pid, p.nome AS pnome, p.categoria, p.prezzo, p.quantita
            FROM   ordini o
            JOIN   clienti  c ON o.cliente_id  = c.id
            JOIN   prodotti p ON o.prodotto_id = p.id
            ORDER  BY o.id
        """;
        try (Statement stmt = getConn().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt   ("cid"),
                        rs.getString("nome"),
                        rs.getString("email")
                );
                Prodotto prodotto = new Prodotto(
                        rs.getInt   ("pid"),
                        rs.getString("pnome"),
                        rs.getString("categoria"),
                        rs.getDouble("prezzo"),
                        rs.getInt   ("quantita")
                );
                LocalDate data = rs.getDate("data_ordine").toLocalDate();
                Ordine o = new Ordine(rs.getInt("id"), cliente, prodotto, rs.getInt("quantita_ordinata"), data);
                lista.add(o);
            }
        }
        return lista;
    }

}
