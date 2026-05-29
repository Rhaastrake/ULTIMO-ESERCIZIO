package it.negozio.dao;

import it.negozio.db.DatabaseManager;
import it.negozio.model.Prodotto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdottoDAO {

    private Connection getConn() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    // ── CREATE
    public void inserisci(Prodotto prodotto) throws SQLException {
        String sql = "INSERT INTO prodotti (nome, categoria, prezzo, quantita) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = getConn(). prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getCategoria());
            ps.setDouble(3, prodotto.getPrezzo());
            ps.setInt(4, prodotto.getQuantita());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()){
                if(keys.next()){
                    System.out.println("Prodotto inserito con id " + keys.getInt(1));
                }
            }
        }
    }

    // ── READ ALL
    public List<Prodotto> trovaTutti() throws SQLException {
        List<Prodotto> lista = new ArrayList<>();
        String sql = "SELECT * FROM prodotti order by id";
        try(Statement s = getConn().createStatement()){
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                lista.add(new Prodotto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getDouble("prezzo"),
                        rs.getInt("quantita")
                ));
            }
        }
        return lista;
    }

    // ── READ BY ID
    public Optional<Prodotto> trovaPerId(int id) throws SQLException {
        String sql = "SELECT * FROM prodotti WHERE id = ?";
        try(PreparedStatement ps = getConn(). prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return Optional.of(new Prodotto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getDouble("prezzo"),
                        rs.getInt("quantita")
                ));
            }
        }
        return Optional.empty();
    }

    // ── READ BY CATEGORIA
    public List<Prodotto> trovaPerCategoria(String categoria) throws SQLException {
        List<Prodotto> lista = new ArrayList<>();
        String sql = "SELECT * FROM prodotti WHERE LOWER(categoria) LIKE LOWER(?)";
        try(PreparedStatement ps = getConn().prepareStatement(sql)){
            ps.setString(1, "%"+categoria+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                lista.add(new Prodotto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getDouble("prezzo"),
                        rs.getInt("quantita")
                ));
            }
        }
        return lista;
    }

    // ── READ con quantita > 0
    public List<Prodotto> trovaDisponibili() throws SQLException {
        List<Prodotto> lista = new ArrayList<>();
        String sql = "SELECT * FROM prodotti WHERE quantita > 0 order by nome";
        try(Statement s = getConn().createStatement()){
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                lista.add(new Prodotto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("categoria"),
                        rs.getDouble("prezzo"),
                        rs.getInt("quantita")
                ));
            }
        }
        return lista;
    }

}
