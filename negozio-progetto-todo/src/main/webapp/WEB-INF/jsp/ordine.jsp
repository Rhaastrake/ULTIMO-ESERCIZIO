<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Negozio — Nuovo Ordine</title>
    <style>
        body    { font-family: Arial, sans-serif; margin: 30px; background: #f5f5f5; }
        h1      { color: #1a5c2a; }
        .nav a  { margin-right: 15px; color: #2e7d32; text-decoration: none; font-weight: bold; }
        .card   { background: white; padding: 25px; border-radius: 6px;
                  max-width: 520px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
        label   { display: block; margin-top: 15px; font-weight: bold; color: #333; }
        select, input[type="number"] {
                  width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc;
                  border-radius: 4px; font-size: 14px; box-sizing: border-box; }
        button  { margin-top: 20px; padding: 10px 20px; background: #2e7d32;
                  color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 15px; }
        button:hover { background: #1a5c2a; }
        .errore { background: #ffcdd2; border: 1px solid #c62828;
                  padding: 10px; margin-bottom: 15px; border-radius: 4px; color: #b71c1c; }
        .info   { color: #666; font-size: 13px; margin-top: 5px; }
    </style>
</head>
<body>

<h1>🛒 Negozio — Magazzino</h1>
<div class="nav" style="margin-bottom:20px;">
    <a href="${pageContext.request.contextPath}/prodotti">← Torna ai prodotti</a>
</div>

<div class="card">
    <h2 style="color:#1a5c2a; margin-top:0;">Nuovo Ordine</h2>

    <c:if test="${not empty param.errore}">
        <div class="errore">
            <c:choose>
                <c:when test="${param.errore == 'campi_mancanti'}">Compila tutti i campi del form.</c:when>
                <c:when test="${param.errore == 'quantita_non_valida'}">La quantità deve essere almeno 1.</c:when>
                <c:when test="${param.errore == 'scorte_insufficienti'}">Scorte insufficienti per la quantità richiesta.</c:when>
                <c:when test="${param.errore == 'non_trovato'}">Prodotto o cliente non trovato nel database.</c:when>
                <c:when test="${param.errore == 'formato_non_valido'}">Valore non numerico inserito.</c:when>
                <c:otherwise>Errore durante la creazione dell'ordine.</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/ordine" method="post">

        <label for="prodottoId">Prodotto:</label>
        <select name="prodottoId" id="prodottoId" required>
            <option value="">-- Seleziona un prodotto --</option>
            <c:forEach var="p" items="${prodottiDisponibili}">
                <option value="${p.id}">
                    ${p.nome} — €<fmt:formatNumber value="${p.prezzo}" minFractionDigits="2" maxFractionDigits="2"/>
                    (disponibili: ${p.quantita})
                </option>
            </c:forEach>
        </select>
        <p class="info">${prodottiDisponibili.size()} prodotti con scorte disponibili</p>

        <label for="clienteId">Cliente:</label>
        <select name="clienteId" id="clienteId" required>
            <option value="">-- Seleziona un cliente --</option>
            <c:forEach var="c" items="${clienti}">
                <option value="${c.id}">${c.nome} (${c.email})</option>
            </c:forEach>
        </select>

        <label for="quantita">Quantità:</label>
        <input type="number" name="quantita" id="quantita" min="1" value="1" required />
        <p class="info">Inserisci quanti pezzi vuoi ordinare.</p>

        <button type="submit">Crea Ordine</button>
    </form>
</div>

</body>
</html>
