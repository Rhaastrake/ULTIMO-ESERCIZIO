package it.biblioteca;

import it.biblioteca.hibernate.*;
import it.biblioteca.model.Libro;
import it.biblioteca.model.Prestito;
import it.biblioteca.model.Utente;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println(" Hibernate ORM");
        System.out.println("==============================================\n");

        LibroHibernateDAO dao = new LibroHibernateDAO();
    }
}
