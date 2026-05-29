package it.biblioteca.hibernate;

import it.biblioteca.model.Libro;
import it.biblioteca.model.Prestito;
import it.biblioteca.model.Utente;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * SessionFactory:
 *   E il cuore di Hibernate: legge la configurazione, crea le connessioni,
 *   gestisce il cache di secondo livello. Va creata UNA VOLTA sola
 *   all'avvio dell'applicazione (e costosa da creare).
 *   Corrisponde al DataSource/pool di connessioni in JDBC.
 *
 * Session:
 *   E l'unita di lavoro con il database. Si apre per ogni operazione
 *   (o gruppo di operazioni correlate) e si chiude subito dopo.
 *   Corrisponde a una Connection in JDBC.
 *   Gestisce il "first-level cache": nella stessa Session, Hibernate
 *   non carica due volte lo stesso oggetto dal DB.
 *
 * Transaction:
 *   Gruppo di operazioni atomiche. Se una fallisce, tutte vengono
 *   annullate (rollback). Obbligatoria per INSERT, UPDATE, DELETE.
 *
 * Configurazione programmatica (senza hibernate.cfg.xml):
 *   Usiamo Configuration() in codice invece del file XML.
 *   Piu semplice per il corso; in produzione si usa persistence.xml.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration config = new Configuration();

            // ── Connessione al database
            config.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            config.setProperty("hibernate.connection.url",
                    "jdbc:mysql://localhost:3306/biblioteca?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
            config.setProperty("hibernate.connection.username", "root");
            config.setProperty("hibernate.connection.password", "1234");

            // ── Dialetto: dice a Hibernate come generare SQL per MySQL
            config.setProperty("hibernate.dialect",
                    "org.hibernate.dialect.MySQL8Dialect");

            // ── Schema: crea/aggiorna le tabelle automaticamente
            // update = crea se non esistono, aggiorna se cambiano le entity
            // create = distrugge e ricrea ad ogni avvio (solo per sviluppo)
            // validate = verifica che le tabelle corrispondano alle entity
            config.setProperty("hibernate.hbm2ddl.auto", "update");

            // ── Log SQL: mostra le query generate da Hibernate (utile per imparare)
            config.setProperty("hibernate.show_sql", "false");
            config.setProperty("hibernate.format_sql", "false");

            // ── Registra le entity (classi mappate)
            config.addAnnotatedClass(Libro.class);
            config.addAnnotatedClass(Utente.class);
            config.addAnnotatedClass(Prestito.class);

            sessionFactory = config.buildSessionFactory();
            System.out.println("[Hibernate] SessionFactory creata con successo.");

        } catch (Exception e) {
            System.err.println("[Hibernate] Errore nella creazione della SessionFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("[Hibernate] SessionFactory chiusa.");
        }
    }
}
