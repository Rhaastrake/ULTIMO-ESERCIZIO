package it.biblioteca.servlet;

import it.biblioteca.dao.LibroDAO;
import it.biblioteca.dao.PrestitoDAO;
import it.biblioteca.dao.UtenteDAO;
import it.biblioteca.model.Libro;
import it.biblioteca.model.Prestito;
import it.biblioteca.model.Utente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * GIORNO 6 — PrestitoServlet
 *
 * Gestisce il form per creare un nuovo prestito.
 *
 * Pattern POST-REDIRECT-GET:
 * Il form invia i dati con POST → la Servlet salva → redirect a GET.
 * Questo evita che premendo F5 il form venga reinviato.
 *
 * Flusso:
 * 1. GET  /prestito          → mostra il form (lista libri + lista utenti)
 * 2. POST /prestito          → salva il prestito nel DB
 * 3. redirect GET /libri     → mostra la lista aggiornata
 */
@WebServlet("/prestito")
public class PrestitoServlet extends HttpServlet {

    private LibroDAO    libroDAO;
    private UtenteDAO   utenteDAO;
    private PrestitoDAO prestitoDAO;

    @Override
    public void init() throws ServletException {
        libroDAO    = new LibroDAO();
        utenteDAO   = new UtenteDAO();
        prestitoDAO = new PrestitoDAO();
    }

    /**
     * GET /prestito → mostra il form con libri disponibili e utenti
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Carica libri disponibili e utenti per popolare i menu a tendina
            req.setAttribute("libriDisponibili", libroDAO.trovaDisponibili());
            req.setAttribute("utenti",           utenteDAO.trovaTutti());

            req.getRequestDispatcher("/WEB-INF/jsp/prestito.jsp")
               .forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Errore database", e);
        }
    }

    /**
     * POST /prestito → riceve i dati del form, salva il prestito, redirect
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // req.getParameter legge i campi del form HTML (name="...")
        String libroIdStr  = req.getParameter("libroId");
        String utenteIdStr = req.getParameter("utenteId");

        // Validazione input
        if (libroIdStr == null || utenteIdStr == null ||
            libroIdStr.isBlank() || utenteIdStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/prestito?errore=campi_mancanti");
            return;
        }

        try {
            int libroId  = Integer.parseInt(libroIdStr);
            int utenteId = Integer.parseInt(utenteIdStr);

            Optional<Libro>  libro  = libroDAO.trovaPerId(libroId);
            Optional<Utente> utente = utenteDAO.trovaPerId(utenteId);

            if (libro.isEmpty() || utente.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/prestito?errore=non_trovato");
                return;
            }

            // Crea e salva il prestito (data inizio = oggi, fine = oggi + 30 giorni)
            Prestito prestito = new Prestito(0, utente.get(), libro.get(), LocalDate.now());
            prestitoDAO.inserisci(prestito);

            // POST-REDIRECT-GET: redirect alla lista libri con messaggio di successo
            resp.sendRedirect(req.getContextPath() + "/libri?messaggio=prestito_creato");

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/prestito?errore=id_non_valido");
        } catch (SQLException e) {
            throw new ServletException("Errore database", e);
        }
    }
}
