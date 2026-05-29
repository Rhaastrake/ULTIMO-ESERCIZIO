package it.biblioteca.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * GIORNO 5 — DatabaseManager
 *
 * Gestisce la connessione al database H2.
 * Applica il pattern SINGLETON (studiato ieri): una sola istanza
 * gestisce tutta la comunicazione con il DB.
 *
 * H2 in modalita FILE: il database viene salvato su disco nella
 * cartella ./data/biblioteca. I dati persistono tra un'esecuzione
 * e l'altra (a differenza della modalita in-memory).
 *
 * CONCETTO CHIAVE per gli studenti:
 * La stringa di connessione JDBC segue sempre questo formato:
 *   jdbc:[driver]:[parametri specifici del driver]
 *
 * Per H2 file: jdbc:h2:./data/biblioteca
 * Per MySQL:   jdbc:mysql://localhost:3306/nomedb
 * Per Postgres:jdbc:postgresql://localhost:5432/nomedb
 * Solo la stringa cambia — il codice JDBC rimane identico.
 */
public class DatabaseManager {

    // ── Costanti di connessione
    private static final String URL      = "jdbc:mysql://localhost:3306/biblioteca?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "1234";

    // ── Singleton
    private static DatabaseManager istanza;
    private Connection connection;

    private DatabaseManager() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("[DB] Connessione H2 stabilita: " + URL);
        inizializzaSchema();
    }

    public static DatabaseManager getInstance() throws SQLException {
        if (istanza == null || istanza.connection.isClosed()) {
            istanza = new DatabaseManager();
        }
        return istanza;
    }

    public Connection getConnection() { return connection; }

    /**
     * Crea le tabelle se non esistono ancora.
     * IF NOT EXISTS: sicuro da chiamare ad ogni avvio — non distrugge dati esistenti.
     */
    private void inizializzaSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // Tabella LIBRI
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS libri (
                    id          INT PRIMARY KEY AUTO_INCREMENT,
                    titolo      VARCHAR(200) NOT NULL,
                    autore      VARCHAR(100) NOT NULL,
                    anno        INT          NOT NULL,
                    disponibile BOOLEAN      NOT NULL DEFAULT TRUE
                )
            """);

            // Tabella UTENTI
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS utenti (
                    id    INT PRIMARY KEY AUTO_INCREMENT,
                    nome  VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE
                )
            """);

            // Tabella PRESTITI
            // FOREIGN KEY: ogni prestito deve riferirsi a un utente e a un libro esistenti
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS prestiti (
                    id           INT PRIMARY KEY AUTO_INCREMENT,
                    utente_id    INT  NOT NULL,
                    libro_id     INT  NOT NULL,
                    data_inizio  DATE NOT NULL,
                    data_fine    DATE NOT NULL,
                    FOREIGN KEY (utente_id) REFERENCES utenti(id),
                    FOREIGN KEY (libro_id)  REFERENCES libri(id)
                )
            """);

            System.out.println("[DB] Schema inizializzato (tabelle: libri, utenti, prestiti)");
        }
    }

    /**
     * Inserisce i dati di esempio SOLO se la tabella libri e vuota.
     * Evita duplicati ad ogni riavvio.
     */
    public void inserisciDatiEsempio() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM libri");
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("[DB] Dati gia presenti, skip inserimento esempi.");
                return;
            }
        }

        String sql = """
            INSERT INTO libri (titolo, autore, anno, disponibile) VALUES
            ('Il Nome della Rosa',        'Umberto Eco',         1980, TRUE),
            ('1984',                      'George Orwell',       1949, TRUE),
            ('Il Signore degli Anelli',   'J.R.R. Tolkien',      1954, TRUE),
            ('Harry Potter',              'J.K. Rowling',        1997, TRUE),
            ('Design Patterns',           'Gang of Four',        1994, TRUE),
            ('Clean Code',                'Robert C. Martin',    2008, TRUE),
            ('Il Pendolo di Foucault',    'Umberto Eco',         1988, TRUE),
            ('La Fattoria degli Animali', 'George Orwell',       1945, TRUE),
            ('Effective Java',            'Joshua Bloch',        2001, TRUE),
            ('Il Processo',               'Franz Kafka',         1925, TRUE)
        """;

        try (Statement stmt = connection.createStatement()) {
            int righe = stmt.executeUpdate(sql);
            System.out.println("[DB] Inseriti " + righe + " libri di esempio.");
        }
    }

    public void chiudi() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("[DB] Connessione chiusa.");
        }
    }
}
