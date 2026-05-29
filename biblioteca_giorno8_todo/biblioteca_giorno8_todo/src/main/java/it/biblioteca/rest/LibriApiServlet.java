package it.biblioteca.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.biblioteca.dao.LibroDAO;
import it.biblioteca.model.Libro;
import it.biblioteca.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * GIORNO 7 — LibriApiServlet
 *
 * REST API per i libri. Endpoint:
 *
 *   GET  /api/libri              → lista tutti i libri (pubblico)
 *   GET  /api/libri?autore=X     → filtra per autore (pubblico)
 *   GET  /api/libri?disponibili=true → solo disponibili (pubblico)
 *   POST /api/libri              → aggiunge un libro (richiede JWT)
 *
 * DIFFERENZA tra Servlet web (giorno 6) e REST API:
 *   Servlet web → forward a JSP → risposta HTML
 *   REST API    → scrive JSON   → risposta JSON
 *
 * CONCETTO CHIAVE — REST:
 *   Representational State Transfer. Principi fondamentali:
 *   1. Stateless: ogni richiesta e indipendente (niente sessioni server)
 *   2. Risorse identificate da URI (/api/libri, /api/libri/5)
 *   3. Operazioni tramite HTTP methods (GET, POST, PUT, DELETE)
 *   4. Rappresentazione in JSON (o XML)
 */
@WebServlet("/api/libri")
public class LibriApiServlet extends HttpServlet {

    private LibroDAO libroDAO;
    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void init() throws ServletException {
        libroDAO = new LibroDAO();
    }

    /**
     * GET /api/libri — lettura pubblica, nessun token richiesto
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        try {
            List<Libro> libri;
            String autore       = req.getParameter("autore");
            String disponibili  = req.getParameter("disponibili");

            if (autore != null && !autore.isBlank()) {
                libri = libroDAO.trovaPerAutore(autore);
            } else if ("true".equals(disponibili)) {
                libri = libroDAO.trovaDisponibili();
            } else {
                libri = libroDAO.trovaTutti();
            }

            // Risposta con wrapper { "totale": N, "libri": [...] }
            var risposta = mapper.createObjectNode();
            risposta.put("totale", libri.size());
            risposta.set("libri", mapper.valueToTree(libri));

            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), risposta);

        } catch (SQLException e) {
            inviaErrore(resp, 500, "Errore database: " + e.getMessage());
        }
    }

    /**
     * POST /api/libri — scrittura protetta da JWT
     * Header richiesto: Authorization: Bearer <token>
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        // ── Verifica JWT nell'header Authorization
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            inviaErrore(resp, 401, "Token JWT mancante. Header: Authorization: Bearer <token>");
            return;
        }

        String token = authHeader.substring(7); // rimuove "Bearer "
        if (!JwtUtil.isTokenValido(token)) {
            inviaErrore(resp, 401, "Token JWT non valido o scaduto.");
            return;
        }

        // Token valido: procedi con l'inserimento
        String username = JwtUtil.estraiUsername(token);

        try {
            // Legge il body JSON: { "titolo": "...", "autore": "...", "anno": ... }
            var body = mapper.readValue(req.getInputStream(), Map.class);

            String titolo = (String) body.get("titolo");
            String autore = (String) body.get("autore");
            Object annoObj = body.get("anno");

            if (titolo == null || autore == null || annoObj == null) {
                inviaErrore(resp, 400, "Campi obbligatori: titolo, autore, anno");
                return;
            }

            int anno = ((Number) annoObj).intValue();
            Libro nuovo = new Libro(0, titolo, autore, anno);
            libroDAO.inserisci(nuovo);

            resp.setStatus(HttpServletResponse.SC_CREATED); // 201
            mapper.writeValue(resp.getWriter(), Map.of(
                "messaggio",       "Libro inserito con successo",
                "inserito_da",     username,
                "titolo",          titolo
            ));

        } catch (SQLException e) {
            inviaErrore(resp, 500, "Errore database: " + e.getMessage());
        } catch (Exception e) {
            inviaErrore(resp, 400, "Richiesta non valida: " + e.getMessage());
        }
    }

    private void inviaErrore(HttpServletResponse resp, int status, String messaggio)
            throws IOException {
        resp.setStatus(status);
        mapper.writeValue(resp.getWriter(), Map.of(
            "errore",  messaggio,
            "status",  status
        ));
    }
}
