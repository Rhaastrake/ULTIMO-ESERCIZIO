package it.ospedale.servlet;

import it.ospedale.dao.MedicoDAO;
import it.ospedale.dao.PazienteDAO;
import it.ospedale.dao.VisitaDAO;
import it.ospedale.model.Medico;
import it.ospedale.model.Paziente;
import it.ospedale.model.Visita;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Pattern POST-REDIRECT-GET — identico a OrdineServlet:
 *
 * 1. GET  /visita  → mostra il form (lista medici + lista pazienti)
 * 2. POST /visita  → valida i dati, salva la visita
 * 3. redirect GET /medici
 */
@WebServlet("/visita")
public class VisitaServlet extends HttpServlet {

    private MedicoDAO   medicoDAO;
    private PazienteDAO pazienteDAO;
    private VisitaDAO   visitaDAO;

    @Override
    public void init() throws ServletException {
        medicoDAO   = new MedicoDAO();
        pazienteDAO = new PazienteDAO();
        visitaDAO   = new VisitaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }
}
