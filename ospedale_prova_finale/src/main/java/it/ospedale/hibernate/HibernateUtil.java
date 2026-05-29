package it.ospedale.hibernate;

import it.ospedale.model.Medico;
import it.ospedale.model.Paziente;
import it.ospedale.model.Visita;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration config = new Configuration();

            config.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            config.setProperty("hibernate.connection.url",
                    "jdbc:mysql://localhost:3306/ospedale?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
            config.setProperty("hibernate.connection.username", "root");
            config.setProperty("hibernate.connection.password", "password123");

            config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            config.setProperty("hibernate.hbm2ddl.auto", "update");
            config.setProperty("hibernate.show_sql", "false");
            config.setProperty("hibernate.format_sql", "false");

            config.addAnnotatedClass(Paziente.class);
            config.addAnnotatedClass(Medico.class);
            config.addAnnotatedClass(Visita.class);

            sessionFactory = config.buildSessionFactory();
            System.out.println("[Hibernate] SessionFactory creata con successo.");

        } catch (Exception e) {
            System.err.println("[Hibernate] Errore: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
