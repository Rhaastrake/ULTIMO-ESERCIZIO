package it.negozio.servlet;

import it.negozio.dao.ProdottoDAO;
import it.negozio.model.Prodotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * GET /negozio/prodotti                   → lista tutti i prodotti
 * GET /negozio/prodotti?categoria=X       → filtra per categoria
 * GET /negozio/prodotti?solo=disponibili  → solo prodotti con quantita > 0
 */
@WebServlet("/prodotti")
public class ProdottiServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        prodottoDAO = new ProdottoDAO();
        System.out.println("[ProdottiServlet] Inizializzata.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            String categoria = req.getParameter("categoria");
            String solo = req.getParameter("solo");
            List<Prodotto> prodotti;
            if(categoria != null && !categoria.isBlank()){
                prodotti = prodottoDAO.trovaPerCategoria(categoria);
                req.setAttribute("filtro", "Categoria: " + categoria);
            } else if("disponibili".equals(solo)){
                prodotti = prodottoDAO.trovaDisponibili();
                req.setAttribute("filtro", "Solo disponibili");
            } else {
                prodotti = prodottoDAO.trovaTutti();
                req.setAttribute("filtro", "Tutti i prodotti");
            }

            req.setAttribute("prodotti", prodotti);
            req.setAttribute("totale", prodotti.size());

            req.getRequestDispatcher("/WEB-INF/jsp/prodotti.jsp")
                    .forward(req, resp);

        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("[ProdottiServlet] Distrutta.");
    }
}
