package it.biblioteca.servlet;

import it.biblioteca.dao.LibroDAO;
import it.biblioteca.model.Libro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * GIORNO 6 — LibriServlet
 *
 * CICLO DI VITA di una Servlet:
 * 1. init()    — chiamato UNA VOLTA quando Tomcat carica la Servlet
 * 2. service() — chiamato per OGNI richiesta HTTP (delega a doGet/doPost)
 * 3. destroy() — chiamato UNA VOLTA quando Tomcat scarica la Servlet
 *
 * CONCETTI CHIAVE per gli studenti:
 *
 * @WebServlet("/libri"): annotation moderna che sostituisce la mappatura
 * in web.xml. Quando arriva una richiesta a /biblioteca/libri,
 * Tomcat usa questa Servlet.
 *
 * doGet() vs doPost():
 * GET  → leggere dati (lista libri, ricerca) — parametri nell'URL
 * POST → inviare/modificare dati (nuovo prestito) — parametri nel body
 *
 * Pattern MVC applicato al web:
 * Servlet = Controller (elabora la richiesta)
 * JSP     = View (mostra i risultati)
 * DAO     = Model (accede ai dati)
 */
@WebServlet("/libri")
public class LibriServlet extends HttpServlet {

    private LibroDAO libroDAO;

    @Override
    public void init() throws ServletException {
        // init() e il posto giusto per inizializzare risorse costose
        libroDAO = new LibroDAO();
        System.out.println("[LibriServlet] Inizializzata.");
    }

    /**
     * GET /biblioteca/libri          → lista tutti i libri
     * GET /biblioteca/libri?autore=X → filtra per autore
     * GET /biblioteca/libri?solo=disponibili → solo disponibili
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Legge i parametri dall'URL (?autore=Orwell)
            String autore = req.getParameter("autore");
            String solo   = req.getParameter("solo");

            List<Libro> libri;

            if (autore != null && !autore.isBlank()) {
                libri = libroDAO.trovaPerAutore(autore);
                req.setAttribute("filtro", "Autore: " + autore);
            } else if ("disponibili".equals(solo)) {
                libri = libroDAO.trovaDisponibili();
                req.setAttribute("filtro", "Solo disponibili");
            } else {
                libri = libroDAO.trovaTutti();
                req.setAttribute("filtro", "Tutti i libri");
            }

            // Mette i dati nel request scope — accessibili dalla JSP con ${libri}
            req.setAttribute("libri", libri);
            req.setAttribute("totale", libri.size());

            // Delega alla JSP per la visualizzazione (forward)
            // NON redirect: il forward avviene lato server, l'URL non cambia
            req.getRequestDispatcher("/WEB-INF/jsp/libri.jsp")
               .forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Errore database: " + e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("[LibriServlet] Distrutta.");
    }
}
