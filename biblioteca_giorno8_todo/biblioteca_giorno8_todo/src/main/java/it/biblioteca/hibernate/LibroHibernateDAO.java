package it.biblioteca.hibernate;

import it.biblioteca.model.Libro;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroHibernateDAO {

    public void inserisci(Libro libro) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            s.persist(libro);
            s.getTransaction().commit();
            System.out.println("Libro inserito");
        }
    }

    public List<Libro> trovaTutti() throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            List<Libro> risultati = new ArrayList<>();
            risultati = s.createQuery("FROM Libro ORDER BY id", Libro.class).list();
            s.getTransaction().commit();
            return risultati;
        }
    }

    public Optional<Libro> trovaPerId(int id) throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            Libro libro = s.get(Libro.class, id);
            s.getTransaction().commit();
            return Optional.ofNullable(libro);
        }
    }

    public List<Libro> trovaPerAutore(String autore) throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            List<Libro> risultati = new ArrayList<>();
            risultati = s.createQuery("FROM Libro WHERE LOWER(autore) LIKE LOWER(?)", Libro.class).list();
            s.getTransaction().commit();
            return risultati;
        }
    }

    public List<Libro> trovaDisponibili() throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            List<Libro> risultati = new ArrayList<>();
            risultati = s.createQuery("FROM Libro WHERE disponibile = TRUE ORDER BY titolo", Libro.class).list();
            s.getTransaction().commit();
            return risultati;
        }
    }

    public void elimina(int id) throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            Libro libro = s.get(Libro.class, id);
            if (libro != null){
                s.remove(libro);
            }
            s.getTransaction().commit();
        }
    }
}
