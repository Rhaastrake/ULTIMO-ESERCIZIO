package it.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "prestiti")
public class Prestito {

    private static final DateTimeFormatter FORMATO_IT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final int DURATA_STANDARD_GIORNI = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne()
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @Column(name = "data_inizio", nullable = false)
    private LocalDate dataInizio;

    @Column(name = "data_fine", nullable = false)
    private LocalDate dataFine;

    protected Prestito() {}

    public Prestito(int id, Utente utente, Libro libro, LocalDate dataInizio) {
        this.id = id;
        this.utente = utente;
        this.libro = libro;
        this.dataInizio = dataInizio;
        this.dataFine = dataInizio.plusDays(DURATA_STANDARD_GIORNI);
        libro.setDisponibile(false);
    }

    /** Nuovo prestito con Hibernate (id generato dal DB). */
    public Prestito(Utente utente, Libro libro, LocalDate dataInizio) {
        this(0, utente, libro, dataInizio);
    }

    public boolean isScaduto() {
        return LocalDate.now().isAfter(dataFine);
    }

    public long giorniRimanenti() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataFine);
    }

    public int getId() { return id; }
    public Utente getUtente() { return utente; }
    public Libro getLibro() { return libro; }
    public LocalDate getDataInizio() { return dataInizio; }
    public LocalDate getDataFine() { return dataFine; }

    public void setId(int id) { this.id = id; }
    public void setUtente(Utente utente) { this.utente = utente; }
    public void setLibro(Libro libro) { this.libro = libro; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    @Override
    public String toString() {
        String stato;
        long giorni = giorniRimanenti();
        if (giorni < 0) {
            stato = "SCADUTO da " + Math.abs(giorni) + " giorni!";
        } else if (giorni == 0) {
            stato = "SCADE OGGI!";
        } else {
            stato = "scade tra " + giorni + " giorni";
        }

        return String.format("Prestito[%d] - %s ha preso \"%s\" il %s (entro il %s — %s)",
                id,
                utente.getNome(),
                libro.getTitolo(),
                dataInizio.format(FORMATO_IT),
                dataFine.format(FORMATO_IT),
                stato);
    }
}
