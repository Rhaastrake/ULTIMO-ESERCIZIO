package it.biblioteca.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.biblioteca.dao.LibroDAO;
import it.biblioteca.dao.PrestitoDAO;
import it.biblioteca.dao.UtenteDAO;
import it.biblioteca.model.Libro;
import it.biblioteca.model.Prestito;
import it.biblioteca.model.Utente;
import it.biblioteca.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

/**
 * GIORNO 7 — PrestitiApiServlet
 *
 * REST API per i prestiti:
 *   GET  /api/prestiti           → lista tutti i prestiti (richiede JWT)
 *   GET  /api/prestiti?scaduti=true → solo scaduti (richiede JWT)
 *   POST /api/prestiti           → crea prestito (richiede JWT)
 *
 * Tutti gli endpoint richiedono autenticazione JWT:
 * la lista dei prestiti contiene dati personali degli utenti.
 */
@WebServlet("/api/prestiti")
public class PrestitiApiServlet extends HttpServlet {

    private PrestitoDAO prestitoDAO;
    private LibroDAO    libroDAO;
    private UtenteDAO   utenteDAO;
    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void init() throws ServletException {
        prestitoDAO = new PrestitoDAO();
        libroDAO    = new LibroDAO();
        utenteDAO   = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        if (!verificaJwt(req, resp)) return;

        try {
            var prestiti = "true".equals(req.getParameter("scaduti"))
                    ? prestitoDAO.trovaScaduti()
                    : prestitoDAO.trovaTutti();

            var risposta = mapper.createObjectNode();
            risposta.put("totale", prestiti.size());
            risposta.set("prestiti", mapper.valueToTree(prestiti));

            resp.setStatus(200);
            mapper.writeValue(resp.getWriter(), risposta);

        } catch (SQLException e) {
            inviaErrore(resp, 500, "Errore database: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        if (!verificaJwt(req, resp)) return;

        try {
            var body     = mapper.readValue(req.getInputStream(), Map.class);
            int libroId  = ((Number) body.get("libroId")).intValue();
            int utenteId = ((Number) body.get("utenteId")).intValue();

            Optional<Libro>  libro  = libroDAO.trovaPerId(libroId);
            Optional<Utente> utente = utenteDAO.trovaPerId(utenteId);

            if (libro.isEmpty())  { inviaErrore(resp, 404, "Libro ID " + libroId + " non trovato");  return; }
            if (utente.isEmpty()) { inviaErrore(resp, 404, "Utente ID " + utenteId + " non trovato"); return; }
            if (!libro.get().isDisponibile()) { inviaErrore(resp, 409, "Libro non disponibile"); return; }

            Prestito p = new Prestito(0, utente.get(), libro.get(), LocalDate.now());
            prestitoDAO.inserisci(p);

            resp.setStatus(201);
            mapper.writeValue(resp.getWriter(), Map.of(
                "messaggio",  "Prestito creato",
                "libro",      libro.get().getTitolo(),
                "utente",     utente.get().getNome(),
                "scade_il",   p.getDataFine().toString()
            ));

        } catch (SQLException e) {
            inviaErrore(resp, 500, "Errore database: " + e.getMessage());
        } catch (Exception e) {
            inviaErrore(resp, 400, "Richiesta non valida: " + e.getMessage());
        }
    }

    /** Estrae e verifica il JWT dall'header Authorization. */
    private boolean verificaJwt(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ") ||
            !JwtUtil.isTokenValido(auth.substring(7))) {
            inviaErrore(resp, 401, "Token JWT mancante o non valido");
            return false;
        }
        return true;
    }

    private void inviaErrore(HttpServletResponse resp, int status, String msg)
            throws IOException {
        resp.setStatus(status);
        mapper.writeValue(resp.getWriter(), Map.of("errore", msg, "status", status));
    }
}
