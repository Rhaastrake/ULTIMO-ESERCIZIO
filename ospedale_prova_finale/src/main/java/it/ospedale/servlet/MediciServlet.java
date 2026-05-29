package it.ospedale.servlet;

import it.ospedale.dao.MedicoDAO;
import it.ospedale.model.Medico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

      try {
         String specializzazione = req.getParameter("specializzazione");
         List<Medico> medici;

         if (specializzazione != null && !specializzazione.isBlank()) {
            medici = medicoDAO.trovaPerSpecializzazione(specializzazione);
            req.setAttribute("filtro", "Specializzazione: " + specializzazione);
         } else {
            medici = medicoDAO.trovaTutti();
            req.setAttribute("filtro", "Tutti i medici");
         }

         req.setAttribute("medici", medici);
         req.setAttribute("totale", medici.size());

         req.getRequestDispatcher("/WEB-INF/jsp/medici.jsp")
                 .forward(req, resp);

      } catch (SQLException e) {
         throw new ServletException("Errore database: " + e.getMessage(), e);
      }
   }

   @Override
   public void destroy() {
      System.out.println("[MediciServlet] Distrutta.");
   }
}