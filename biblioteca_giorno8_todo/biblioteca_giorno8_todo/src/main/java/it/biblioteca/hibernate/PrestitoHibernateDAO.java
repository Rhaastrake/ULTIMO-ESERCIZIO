package it.biblioteca.hibernate;

import it.biblioteca.model.Libro;
import it.biblioteca.model.Prestito;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrestitoHibernateDAO {
    public void inserisci(Prestito prestito) throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction t = s.beginTransaction();

            s.persist(prestito);

            t.commit();
            System.out.println("Prestito inserito");
        }
    }

    public List<Prestito> trovaTutti() throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            List<Prestito> risultati = s.createQuery("FROM Prestito ORDER BY id", Prestito.class).list();
            s.getTransaction().commit();
            return risultati;
        }
    }

    public List<Prestito> trovaScaduti() throws SQLException {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            s.beginTransaction();
            List<Prestito> risultati = s.createQuery("FROM Prestito WHERE dataFine < :oggi ORDER BY dataFine", Prestito.class)
                    .setParameter("oggi", LocalDate.now())
                    .list();
            s.getTransaction().commit();
            return risultati;
        }
    }


}
