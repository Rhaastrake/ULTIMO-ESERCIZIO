package it.ospedale.dao;

import it.ospedale.db.DBManager;
import it.ospedale.model.Medico;
import it.ospedale.model.Paziente;
import it.ospedale.model.Visita;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VisitaDAO {

   private Connection getConn() throws SQLException {
      return DBManager.getInstance().getConnection();
   }

   public void inserisci(Visita visita) throws SQLException {
      String sql = """
            INSERT INTO visite (paziente_id, medico_id, data_visita, descrizione)
            VALUES (?, ?, ?, ?)
        """;
      try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         ps.setInt   (1, visita.getPaziente().getId());
         ps.setInt   (2, visita.getMedico().getId());
         ps.setDate  (3, Date.valueOf(visita.getDataVisita()));
         ps.setString(4, visita.getDescrizione());
         ps.executeUpdate();
         try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next())
               System.out.println("[DAO] Visita inserita con ID: " + keys.getInt(1));
         }
      }
   }

   public List<Visita> trovaTutti() throws SQLException {
      List<Visita> lista = new ArrayList<>();
      String sql = """
            SELECT v.id, v.data_visita, v.descrizione,
                   p.id AS pid, p.nome AS pnome, p.email AS pemail,
                   m.id AS mid, m.nome AS mnome, m.specializzazione
            FROM   visite v
            JOIN   pazienti p ON v.paziente_id = p.id
            JOIN   medici   m ON v.medico_id   = m.id
            ORDER  BY v.id DESC
        """;
      try (Statement stmt = getConn().createStatement();
           ResultSet rs   = stmt.executeQuery(sql)) {
         while (rs.next()) lista.add(mappaRiga(rs));
      }
      return lista;
   }

   private Visita mappaRiga(ResultSet rs) throws SQLException {
      Paziente paziente = new Paziente(
              rs.getInt   ("pid"),
              rs.getString("pnome"),
              rs.getString("pemail")
      );
      Medico medico = new Medico(
              rs.getInt   ("mid"),
              rs.getString("mnome"),
              rs.getString("specializzazione")
      );
      LocalDate data = rs.getDate("data_visita").toLocalDate();
      return new Visita(
              rs.getInt   ("id"),
              paziente,
              medico,
              data,
              rs.getString("descrizione")
      );
   }
}