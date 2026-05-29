<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca — Nuovo Prestito</title>
    <style>
        body     { font-family: Arial, sans-serif; margin: 30px; background: #f5f5f5; }
        h1       { color: #1F4E79; }
        .nav a   { margin-right: 15px; color: #2E75B6; text-decoration: none; font-weight: bold; }
        .card    { background: white; padding: 25px; border-radius: 6px;
                   max-width: 500px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); }
        label    { display: block; margin-top: 15px; font-weight: bold; color: #333; }
        select   { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ccc;
                   border-radius: 4px; font-size: 14px; }
        button   { margin-top: 20px; padding: 10px 20px; background: #1F4E79;
                   color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 15px; }
        button:hover { background: #2E75B6; }
        .errore  { background: #FCE4D6; border: 1px solid #C00000;
                   padding: 10px; margin-bottom: 15px; border-radius: 4px; color: #C00000; }
        .info    { color: #555; font-size: 13px; margin-top: 5px; }
    </style>
</head>
<body>

<h1>📚 Sistema Biblioteca</h1>
<div class="nav" style="margin-bottom:20px;">
    <a href="${pageContext.request.contextPath}/libri">← Torna alla lista</a>
</div>

<div class="card">
    <h2 style="color:#1F4E79; margin-top:0;">Nuovo Prestito</h2>

    <%-- Mostra errore se presente nell'URL (?errore=...) --%>
    <c:if test="${not empty param.errore}">
        <div class="errore">
            <c:choose>
                <c:when test="${param.errore == 'campi_mancanti'}">Seleziona sia il libro che l'utente.</c:when>
                <c:when test="${param.errore == 'non_trovato'}">Libro o utente non trovato nel database.</c:when>
                <c:otherwise>Errore durante la creazione del prestito.</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <%--
        Il form usa method="post": i dati vengono inviati nel body HTTP,
        non nell'URL. Adatto per operazioni che modificano dati.
        action punta alla PrestitoServlet che gestisce il POST.
    --%>
    <form action="${pageContext.request.contextPath}/prestito" method="post">

        <label for="libroId">Libro da prestare:</label>
        <select name="libroId" id="libroId" required>
            <option value="">-- Seleziona un libro --</option>
            <%-- Popola il menu con i libri disponibili passati dalla Servlet --%>
            <c:forEach var="libro" items="${libriDisponibili}">
                <option value="${libro.id}">
                    ${libro.titolo} — ${libro.autore} (${libro.anno})
                </option>
            </c:forEach>
        </select>
        <p class="info">Mostrati solo i libri attualmente disponibili (${libriDisponibili.size()} libri)</p>

        <label for="utenteId">Utente:</label>
        <select name="utenteId" id="utenteId" required>
            <option value="">-- Seleziona un utente --</option>
            <c:forEach var="utente" items="${utenti}">
                <option value="${utente.id}">${utente.nome} (${utente.email})</option>
            </c:forEach>
        </select>

        <p class="info" style="margin-top:15px;">
            Il prestito durerà 30 giorni dalla data odierna.
        </p>

        <button type="submit">Crea Prestito</button>
    </form>
</div>

</body>
</html>
