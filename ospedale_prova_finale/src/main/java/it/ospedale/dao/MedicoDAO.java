package it.ospedale.dao;

import it.ospedale.db.DBManager;
import it.ospedale.model.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicoDAO {

   private Connection getConn() throws SQLException {
      return DBManager.getInstance().getConnection();
   }

   public void inserisci(Medico medico) throws SQLException {
      String sql = "INSERT INTO medici (nome, specializzazione) VALUES (?, ?)";
      try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         ps.setString(1, medico.getNome());
         ps.setString(2, medico.getSpecializzazione());
         ps.executeUpdate();
         try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next())
               System.out.println("[DAO] Medico inserito con ID: " + keys.getInt(1));
         }
      }
   }

   public List<Medico> trovaTutti() throws SQLException {
      List<Medico> lista = new ArrayList<>();
      String sql = "SELECT * FROM medici ORDER BY id";
      try (Statement stmt = getConn().createStatement();
           ResultSet rs   = stmt.executeQuery(sql)) {
         while (rs.next()) lista.add(mappaRiga(rs));
      }
      return lista;
   }

   public Optional<Medico> trovaPerId(int id) throws SQLException {
      String sql = "SELECT * FROM medici WHERE id = ?";
      try (PreparedStatement ps = getConn().prepareStatement(sql)) {
         ps.setInt(1, id);
         try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return Optional.of(mappaRiga(rs));
         }
      }
      return Optional.empty();
   }

   public List<Medico> trovaPerSpecializzazione(String specializzazione) throws SQLException {
      List<Medico> lista = new ArrayList<>();
      String sql = "SELECT * FROM medici WHERE LOWER(specializzazione) LIKE LOWER(?) ORDER BY nome";
      try (PreparedStatement ps = getConn().prepareStatement(sql)) {
         ps.setString(1, "%" + specializzazione + "%");
         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mappaRiga(rs));
         }
      }
      return lista;
   }

   private Medico mappaRiga(ResultSet rs) throws SQLException {
      return new Medico(
              rs.getInt   ("id"),
              rs.getString("nome"),
              rs.getString("specializzazione")
      );
   }
}