package it.ospedale.dao;

import it.ospedale.db.DBManager;
import it.ospedale.model.Paziente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PazienteDAO {

   private Connection getConn() throws SQLException {
      return DBManager.getInstance().getConnection();
   }

   public void inserisci(Paziente paziente) throws SQLException {
      String sql = "INSERT INTO pazienti (nome, email) VALUES (?, ?)";
      try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         ps.setString(1, paziente.getNome());
         ps.setString(2, paziente.getEmail());
         ps.executeUpdate();
         try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next())
               System.out.println("[DAO] Paziente inserito con ID: " + keys.getInt(1));
         }
      }
   }

   public List<Paziente> trovaTutti() throws SQLException {
      List<Paziente> lista = new ArrayList<>();
      String sql = "SELECT * FROM pazienti ORDER BY id";
      try (Statement stmt = getConn().createStatement();
           ResultSet rs   = stmt.executeQuery(sql)) {
         while (rs.next()) lista.add(mappaRiga(rs));
      }
      return lista;
   }

   public Optional<Paziente> trovaPerId(int id) throws SQLException {
      String sql = "SELECT * FROM pazienti WHERE id = ?";
      try (PreparedStatement ps = getConn().prepareStatement(sql)) {
         ps.setInt(1, id);
         try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return Optional.of(mappaRiga(rs));
         }
      }
      return Optional.empty();
   }

   private Paziente mappaRiga(ResultSet rs) throws SQLException {
      return new Paziente(
              rs.getInt   ("id"),
              rs.getString("nome"),
              rs.getString("email")
      );
   }
}