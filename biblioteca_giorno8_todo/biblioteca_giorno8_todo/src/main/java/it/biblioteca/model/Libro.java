package it.biblioteca.model;

import jakarta.persistence.*;

/**
 * Modello del dominio (JDBC, servlet, REST) e entity JPA (Hibernate).
 * Le annotazioni @Entity mappano questa classe alla tabella "libri".
 */
@Entity
@Table(name = "libri")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "titolo", nullable = false, length = 200)
    private String titolo;

    @Column(name = "autore", nullable = false, length = 100)
    private String autore;

    @Column(name = "anno", nullable = false)
    private int anno;

    @Column(name = "disponibile", nullable = false)
    private boolean disponibile = true;

    /** Obbligatorio per Hibernate. */
    protected Libro() {}

    /** Costruttore usato da JDBC e dalle API (id già noto). */
    public Libro(int id, String titolo, String autore, int anno) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;
    }

    /** Costruttore per nuovi libri con Hibernate (id generato dal DB). */
    public Libro(String titolo, String autore, int anno) {
        this(0, titolo, autore, anno);
    }

    public int getId() { return id; }
    public String getTitolo() { return titolo; }
    public String getAutore() { return autore; }
    public int getAnno() { return anno; }
    public boolean isDisponibile() { return disponibile; }

    public void setId(int id) { this.id = id; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public void setAutore(String autore) { this.autore = autore; }
    public void setAnno(int anno) { this.anno = anno; }
    public void setDisponibile(boolean disponibile) { this.disponibile = disponibile; }

    @Override
    public String toString() {
        return String.format("[%d] \"%s\" di %s (%d) - %s",
                id, titolo, autore, anno, disponibile ? "disponibile" : "non disponibile");
    }
}
