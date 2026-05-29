package it.biblioteca.model;

import jakarta.persistence.*;
import java.util.regex.Pattern;

/**
 * Modello del dominio e entity JPA.
 * La validazione email resta nel costruttore con parametri (JDBC, REST).
 */
@Entity
@Table(name = "utenti")
public class Utente {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    protected Utente() {}

    public Utente(int id, String nome, String email) {
        this.id = id;
        setNome(nome);
        setEmail(email);
    }

    /** Nuovo utente con Hibernate (id generato dal DB). */
    public Utente(String nome, String email) {
        this(0, nome, email);
    }

    public static boolean isEmailValida(String email) {
        if (email == null || email.isBlank()) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }

    public void setId(int id) { this.id = id; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        if (!isEmailValida(email)) {
            throw new IllegalArgumentException(
                "Email non valida: '" + email + "'. Formato atteso: nome@dominio.ext");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", id, nome, email);
    }
}
