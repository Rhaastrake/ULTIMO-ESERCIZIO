package it.ospedale.servlet;

import it.ospedale.dao.MedicoDAO;
import it.ospedale.model.Medico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * GET /ospedale/medici                        → lista tutti i medici
 * GET /ospedale/medici?specializzazione=X     → filtra per specializzazione
 */
@WebServlet("/medici")
public class MediciServlet extends HttpServlet {

    private MedicoDAO medicoDAO;

    @Override
    public void init() throws ServletException {
        medicoDAO = new MedicoDAO();
        System.out.println("[MediciServlet] Inizializzata.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }

    @Override
    public void destroy() {
        System.out.println("[MediciServlet] Distrutta.");
    }
}
