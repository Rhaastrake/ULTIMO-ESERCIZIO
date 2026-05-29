package it.biblioteca.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.biblioteca.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * GIORNO 7 — LoginServlet
 *
 * Endpoint REST per l'autenticazione:
 *   POST /api/login
 *   Body: { "username": "admin", "password": "1234" }
 *   Risposta 200: { "token": "eyJ..." }
 *   Risposta 401: { "errore": "Credenziali non valide" }
 *
 * NOTA DIDATTICA:
 * In questo esempio le credenziali sono hardcoded per semplicita.
 * In produzione si verificherebbero contro il database (con password
 * hashata con BCrypt, MAI in chiaro).
 */
@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    // Credenziali hardcoded per il corso — in produzione: database + BCrypt
    private static final Map<String, String> UTENTI = Map.of(
        "admin",     "1234",
        "biblioteca","pass"
    );
    private static final Map<String, String> RUOLI = Map.of(
        "admin",     "ADMIN",
        "biblioteca","USER"
    );

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        try {
            // Legge il body JSON della richiesta
            var body = mapper.readValue(req.getInputStream(), Map.class);
            String username = (String) body.get("username");
            String password = (String) body.get("password");

            // Verifica credenziali
            if (username == null || password == null ||
                !password.equals(UTENTI.get(username))) {

                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                mapper.writeValue(resp.getWriter(),
                    Map.of("errore", "Credenziali non valide"));
                return;
            }

            // Genera il token JWT
            String ruolo = RUOLI.get(username);
            String token = JwtUtil.generaToken(username, ruolo);

            resp.setStatus(HttpServletResponse.SC_OK); // 200
            mapper.writeValue(resp.getWriter(), Map.of(
                "token",    token,
                "username", username,
                "ruolo",    ruolo,
                "scade_in", "3600 secondi"
            ));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
            mapper.writeValue(resp.getWriter(),
                Map.of("errore", "Richiesta non valida: " + e.getMessage()));
        }
    }
}
