package it.negozio.servlet;

import it.negozio.dao.ClienteDAO;
import it.negozio.dao.OrdineDAO;
import it.negozio.dao.ProdottoDAO;
import it.negozio.model.Cliente;
import it.negozio.model.Ordine;
import it.negozio.model.Prodotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Pattern POST-REDIRECT-GET:
 * 1. GET  /ordine   → mostra il form (prodotti disponibili + clienti)
 * 2. POST /ordine   → valida i dati, controlla le scorte, salva
 * 3. redirect GET /prodotti
 */
@WebServlet("/ordine")
public class OrdineServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;
    private ClienteDAO  clienteDAO;
    private OrdineDAO   ordineDAO;

    @Override
    public void init() throws ServletException {
        prodottoDAO = new ProdottoDAO();
        clienteDAO  = new ClienteDAO();
        ordineDAO   = new OrdineDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            req.setAttribute("prodottiDisponibili", prodottoDAO.trovaDisponibili());
            req.setAttribute("clienti", clienteDAO.trovaTutti());
            req.getRequestDispatcher("/WEB-INF/jsp/ordine.jsp")
                    .forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String prodottoIdStr  = req.getParameter("prodottoId");
        String clienteIdStr = req.getParameter("clienteId");
        String quantitaStr = req.getParameter("quantita");

        if(prodottoIdStr == null || clienteIdStr == null || quantitaStr == null){
            resp.sendRedirect(req.getContextPath() + "ordine?errore=campi_mancanti");
            return;
        }

        int prodottoId = Integer.parseInt(prodottoIdStr);
        int clienteId = Integer.parseInt(clienteIdStr);
        int quantita = Integer.parseInt(quantitaStr);

        if(quantita <= 0){
            resp.sendRedirect(req.getContextPath() + "ordine?errore=quantita_non_valida");
            return;
        }

        try {
            Optional<Prodotto> prodotto = prodottoDAO.trovaPerId(prodottoId);
            Optional<Cliente> cliente = clienteDAO.trovaPerId(clienteId);
            if(prodotto.isEmpty() || cliente.isEmpty()){
                resp.sendRedirect(req.getContextPath() + "/ordine?errore=non_trovato");
                return;
            }
            if(prodotto.get().getQuantita() < quantita){
                resp.sendRedirect(req.getContextPath() + "/ordine?errore=scorte_insufficienti");
                return;
            }

            Ordine ordine = new Ordine(0, cliente.get(), prodotto.get(), quantita, LocalDate.now());
            ordineDAO.inserisci(ordine);
            resp.sendRedirect(req.getContextPath() + "/prodotti?messaggio=ordine_creato");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
