package it.ospedale.servlet;

import it.ospedale.dao.VisitaDAO;
import it.ospedale.model.Visita;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/lista-visite")
public class ListaVisiteServlet extends HttpServlet {

   private VisitaDAO visitaDAO;

   @Override
   public void init() throws ServletException {
      visitaDAO = new VisitaDAO();
      System.out.println("[VisiteServlet] Inizializzata.");
   }

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
           throws ServletException, IOException {
      try {
         List<Visita> visite = visitaDAO.trovaTutti();

         req.setAttribute("visite", visite);
         req.setAttribute("totale", visite.size());

         req.getRequestDispatcher("/WEB-INF/jsp/lista-visite.jsp")
                 .forward(req, resp);

      } catch (SQLException e) {
         throw new ServletException("Errore database: " + e.getMessage(), e);
      }
   }

   @Override
   public void destroy() {
      System.out.println("[VisiteServlet] Distrutta.");
   }
}