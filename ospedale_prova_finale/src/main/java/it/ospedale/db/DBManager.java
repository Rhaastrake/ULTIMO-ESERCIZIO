package it.ospedale.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

   private static final Dotenv dotenv = Dotenv.configure()
           .ignoreIfMissing()
           .load();

   private static final String URL = dotenv.get("DB_URL");
   private static final String USER = dotenv.get("DB_USER");
   private static final String PASSWORD = dotenv.get("DB_PASSWORD");

   private static DBManager instance;
   private Connection connection;

   private DBManager() throws SQLException {
      this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("[DB] Connessione stabilita.");
   }

   public static DBManager getInstance() throws SQLException {
      if (instance == null || instance.connection.isClosed()) {
         instance = new DBManager();
      }
      return instance;
   }

   public Connection getConnection() {
      return connection;
   }
}