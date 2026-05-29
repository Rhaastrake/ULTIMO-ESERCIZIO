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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet("/nuova-visita")
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
      try {
         req.setAttribute("medici",   medicoDAO.trovaTutti());
         req.setAttribute("pazienti", pazienteDAO.trovaTutti());

         req.getRequestDispatcher("/WEB-INF/jsp/nuova-visita.jsp")
                 .forward(req, resp);

      } catch (SQLException e) {
         throw new ServletException("Errore database: " + e.getMessage(), e);
      }
   }

   @Override
   protected void doPost(HttpServletRequest req, HttpServletResponse resp)
           throws ServletException, IOException {

      String medicoIdStr   = req.getParameter("medicoId");
      String pazienteIdStr = req.getParameter("pazienteId");
      String descrizione   = req.getParameter("descrizione");

      if (medicoIdStr   == null || medicoIdStr.isBlank()   ||
              pazienteIdStr == null || pazienteIdStr.isBlank() ||
              descrizione   == null || descrizione.isBlank()) {

         resp.sendRedirect(req.getContextPath() + "/nuova-visita?errore=campi_mancanti");
         return;
      }

      try {
         int medicoId   = Integer.parseInt(medicoIdStr);
         int pazienteId = Integer.parseInt(pazienteIdStr);

         Optional<Medico>   medico   = medicoDAO.trovaPerId(medicoId);
         Optional<Paziente> paziente = pazienteDAO.trovaPerId(pazienteId);

         if (medico.isEmpty() || paziente.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/nuova-visita?errore=non_trovato");
            return;
         }

         Visita visita = new Visita(
                 0,
                 paziente.get(),
                 medico.get(),
                 LocalDate.now(),
                 descrizione
         );
         visitaDAO.inserisci(visita);

         resp.sendRedirect(req.getContextPath() + "/medici?messaggio=visita_creata");

      } catch (NumberFormatException e) {
         resp.sendRedirect(req.getContextPath() + "/nuova-visita?errore=formato_non_valido");
      } catch (SQLException e) {
         throw new ServletException("Errore database: " + e.getMessage(), e);
      }
   }
}