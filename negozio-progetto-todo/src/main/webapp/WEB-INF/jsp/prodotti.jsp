<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Negozio — Prodotti</title>
    <style>
        body    { font-family: Arial, sans-serif; margin: 30px; background: #f5f5f5; }
        h1      { color: #1a5c2a; }
        .nav a  { margin-right: 15px; color: #2e7d32; text-decoration: none; font-weight: bold; }
        .filtri { margin: 15px 0; display: flex; gap: 10px; }
        .filtri a { padding: 6px 14px; background: #2e7d32; color: white;
                    border-radius: 4px; text-decoration: none; font-size: 13px; }
        .filtri a:hover { background: #1a5c2a; }
        table   { width: 100%; border-collapse: collapse; background: white;
                  box-shadow: 0 2px 6px rgba(0,0,0,0.1); border-radius: 6px; overflow: hidden; }
        th      { background: #2e7d32; color: white; padding: 12px 15px; text-align: left; }
        td      { padding: 10px 15px; border-bottom: 1px solid #e0e0e0; }
        tr:last-child td { border-bottom: none; }
        tr:hover td { background: #f1f8e9; }
        .badge-ok      { background: #c8e6c9; color: #1b5e20; padding: 3px 8px; border-radius: 10px; font-size: 12px; }
        .badge-basso   { background: #fff9c4; color: #f57f17; padding: 3px 8px; border-radius: 10px; font-size: 12px; }
        .badge-esaurito{ background: #ffcdd2; color: #b71c1c; padding: 3px 8px; border-radius: 10px; font-size: 12px; }
        .successo { background: #c8e6c9; border: 1px solid #388e3c;
                    padding: 10px; margin-bottom: 15px; border-radius: 4px; color: #1b5e20; }
        .info-bar { color: #555; margin-bottom: 12px; }
    </style>
</head>
<body>

<h1>🛒 Negozio — Magazzino</h1>
<div class="nav">
    <a href="${pageContext.request.contextPath}/prodotti">Tutti i prodotti</a>
    <a href="${pageContext.request.contextPath}/ordine">+ Nuovo Ordine</a>
</div>

<c:if test="${param.messaggio == 'ordine_creato'}">
    <div class="successo" style="margin-top:15px;">✅ Ordine creato! Le scorte sono state aggiornate.</div>
</c:if>

<div class="filtri" style="margin-top:15px;">
    <a href="${pageContext.request.contextPath}/prodotti">Tutti</a>
    <a href="${pageContext.request.contextPath}/prodotti?solo=disponibili">Disponibili</a>
    <a href="${pageContext.request.contextPath}/prodotti?categoria=elettronica">Elettronica</a>
    <a href="${pageContext.request.contextPath}/prodotti?categoria=abbigliamento">Abbigliamento</a>
    <a href="${pageContext.request.contextPath}/prodotti?categoria=alimentari">Alimentari</a>
</div>

<div class="info-bar"><strong>${filtro}</strong> — ${totale} prodotti trovati</div>

<table>
    <thead>
        <tr>
            <th>ID</th><th>Nome</th><th>Categoria</th><th>Prezzo</th><th>Quantità</th><th>Stato</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="p" items="${prodotti}">
            <tr>
                <td>${p.id}</td>
                <td><strong>${p.nome}</strong></td>
                <td>${p.categoria}</td>
                <td>€ <fmt:formatNumber value="${p.prezzo}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>${p.quantita}</td>
                <td>
                    <%-- La JSP usa direttamente p.quantita per decidere il badge,
                         senza chiamare nessun isDisponibile() sul model --%>
                    <c:choose>
                        <c:when test="${p.quantita == 0}">
                            <span class="badge-esaurito">ESAURITO</span>
                        </c:when>
                        <c:when test="${p.quantita <= 3}">
                            <span class="badge-basso">Scorte basse</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge-ok">Disponibile</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty prodotti}">
            <tr><td colspan="6" style="text-align:center;color:#999;">Nessun prodotto trovato.</td></tr>
        </c:if>
    </tbody>
</table>

</body>
</html>
