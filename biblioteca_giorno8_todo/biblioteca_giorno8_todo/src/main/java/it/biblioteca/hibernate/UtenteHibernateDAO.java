package it.biblioteca.hibernate;

import it.biblioteca.model.Libro;
import it.biblioteca.model.Utente;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtenteHibernateDAO {
    public void inserisci(Utente utente) throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            s.persist(utente);
            s.getTransaction().commit();
            System.out.println("Utente inserito");
        }
    }

    public List<Utente> trovaTutti() throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            List<Utente> risultati = new ArrayList<>();
            risultati = s.createQuery("FROM utenti ORDER BY id", Utente.class).list();
            s.getTransaction().commit();
            return risultati;
        }
    }

    public Optional<Utente> trovaPerId(int id) throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            Utente utente = s.get(Utente.class, id);
            s.getTransaction().commit();
            return Optional.ofNullable(utente);
        }
    }
}
