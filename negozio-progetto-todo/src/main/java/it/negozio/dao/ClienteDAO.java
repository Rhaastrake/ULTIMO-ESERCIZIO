package it.negozio.dao;

import it.negozio.db.DatabaseManager;
import it.negozio.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO {

    private Connection getConn() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    public void inserisci(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clienti (nome, email) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next())
                    System.out.println("[DAO] Cliente inserito con ID: " + keys.getInt(1));
            }
        }
    }

    public List<Cliente> trovaTutti() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clienti ORDER BY id";
        try (Statement stmt = getConn().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(new Cliente(
                    rs.getInt   ("id"),
                    rs.getString("nome"),
                    rs.getString("email")
            ));
        }
        return lista;
    }

    public Optional<Cliente> trovaPerId(int id) throws SQLException {
        String sql = "SELECT * FROM clienti WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new Cliente(
                        rs.getInt   ("id"),
                        rs.getString("nome"),
                        rs.getString("email")
                ));
            }
        }
        return Optional.empty();
    }

}
